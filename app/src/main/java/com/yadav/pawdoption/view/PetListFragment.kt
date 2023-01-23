package com.yadav.pawdoption.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.PetListAdapter
import com.yadav.pawdoption.databinding.FragmentUserProfileBinding
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.ShelterPet
import com.yadav.pawdoption.model.UserLovedPet
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.SheltersDAO
import com.yadav.pawdoption.persistence.UsersDAO

// Code Reference: https://firebase.google.com/docs/database/android/read-and-write

class PetListFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var petListAdapter: PetListAdapter
    private val sheltersDAO = SheltersDAO()
    private val usersDAO = UsersDAO()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var searchView: SearchView
    private var petsList: MutableList<ShelterPet> = mutableListOf()

    private var _binding: FragmentUserProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pet_list, container, false)

        activity?.title = "Pets"

        searchView = view.findViewById(R.id.searchView)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterList(newText)
                }
                return false
            }
        })

        setupRecyclerView(view)

        val fabAddPet = view.findViewById<FloatingActionButton>(R.id.fabAddPet)

//      Setup bottom navigation bar as per user type
        if (FirebaseDatabaseSingleton.getCurrentUser() == null) {
            FirebaseDatabaseSingleton.setCurrentUser()

            usersDAO.setCurrentUserTypeByUid(FirebaseDatabaseSingleton.getCurrentUid())
            usersDAO.getCurrentUserTypeByUid().observe(viewLifecycleOwner) {
                setBottomNavigation(it)
            }
        }

//      Change add pet FAB visibility as per user type
        if (FirebaseDatabaseSingleton.getCurrentUserType().uppercase().equals("PETADOPTER"))
            fabAddPet.visibility = View.GONE
        else {
            fabAddPet.visibility = View.VISIBLE
        }

        fabAddPet.setOnClickListener {
            val intent = Intent(requireContext(), UploadPet::class.java)
            startActivity(intent);
        }


        return view
    }

//  Filter pet list by pet breed for searching
    private fun filterList(text: String) {
        var filteredList: MutableList<ShelterPet> = mutableListOf()
        for (pet in petsList) {
            if (pet.breed?.lowercase()?.contains(text.lowercase()) == true) {
                filteredList.add(pet)
            }
        }

        petListAdapter.setFilteredList(filteredList)
    }

//    Setup recycler view for pet list
    private fun setupRecyclerView(view: View) {
        petListAdapter = PetListAdapter(requireContext(), mutableListOf())
        linearLayoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = petListAdapter
        sheltersDAO.getShelters().observe(
            viewLifecycleOwner
        ) { item ->

            if (FirebaseDatabaseSingleton.getCurrentUserType().uppercase().equals("PETADOPTER"))
                usersDAO.getLovedPetsByUid(FirebaseDatabaseSingleton.getCurrentUid()).observe(viewLifecycleOwner) {
                    petsList = getAllPets(item, it)
                    petListAdapter = PetListAdapter(requireContext(), petsList)
                    recyclerView.adapter = petListAdapter
                    petListAdapter.notifyDataSetChanged()
                }
            else
                    petsList = getCurrentShelterPets(item)
            petListAdapter = PetListAdapter(requireContext(), petsList)
            recyclerView.adapter = petListAdapter
            petListAdapter.notifyDataSetChanged()
        }
    }

//    Fetch pets for the logged in shelter user
    private fun getCurrentShelterPets(it: HashMap<String, Shelter>): MutableList<ShelterPet> {
        val currentShelterPetList: MutableList<ShelterPet> = mutableListOf()
        if (it.get(FirebaseDatabaseSingleton.getCurrentUid())?.pets != null) {
            for (pet in it.get(FirebaseDatabaseSingleton.getCurrentUid())?.pets!!) {
                if (pet != null) {
                    pet.shelterId =
                        it.get(FirebaseDatabaseSingleton.getCurrentUid())!!.id.toString()
                    pet.shelterName =
                        it.get(FirebaseDatabaseSingleton.getCurrentUid())!!.name.toString()
                    currentShelterPetList.add(pet)
                }
            }
        }
        return currentShelterPetList
    }

//    Get all pets from all shelters for user
    private fun getAllPets(it: HashMap<String, Shelter>, lovedPetsList: HashMap<String,UserLovedPet>): MutableList<ShelterPet> {
        val allPetList: MutableList<ShelterPet> = mutableListOf()
        for (shelter in it) {
            for (pet in shelter.value.pets) {
                if (pet != null) {
                    pet.shelterId = shelter.key
                    pet.shelterName = shelter.value.name.toString()
                    pet.lovedPetsList = lovedPetsList
                    allPetList.add(pet)
                }
            }
        }
        return allPetList
    }

    //    Setup bottom navigation bar as per logged in user type
    fun setBottomNavigation(userType: String) {
        bottomNavigationView = if (userType == "petAdopter")
            activity?.findViewById(R.id.bottom_nav_pet_owner)!!
        else
            activity?.findViewById(R.id.bottom_nav_shelter)!!
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.selectedItemId = R.id.pets
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pets -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.petListFragment)
                    true
                }
                R.id.shelters -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.mapsFragment)
                    true
                }
                R.id.vet -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.bookAppointment)
                    true
                }
                R.id.profile -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.userProfileFragment)
                    true
                }

                R.id.pending -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.pendingAdoptionFragment)
                    true
                }
                R.id.donations -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.shelterAllDonations)
                    true
                }
                R.id.loved -> {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.userDonations)
                    true
                }
                else -> true
            }

        }
    }

}
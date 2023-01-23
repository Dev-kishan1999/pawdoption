//https://www.geeksforgeeks.org/android-image-slider-using-viewpager-in-kotlin/
package com.yadav.pawdoption.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.PetDetailImageCorousalAdapter
import com.yadav.pawdoption.databinding.FragmentPetDetailBinding
import com.yadav.pawdoption.model.PendingAdoption
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.ShelterPet
import com.yadav.pawdoption.model.UserLovedPet
import com.yadav.pawdoption.persistence.*
import me.relex.circleindicator.CircleIndicator
import java.util.*
import kotlin.collections.HashMap

class PetDetailFragment : Fragment() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: PetDetailImageCorousalAdapter
    lateinit var indicator: CircleIndicator

    var _binding: FragmentPetDetailBinding? = null
    var liked: String? = null;
    var shelter: Shelter? = null;
    var pet: ShelterPet? = null;

    var userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: "uid1"
    var petId: String = "0"
    var shelterId: String = "2001"

    private val binding get() = _binding!!

    val args: PetDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        activity?.title = "Pet details"

        _binding = FragmentPetDetailBinding.inflate(inflater, container, false)

        if(FirebaseDatabaseSingleton.getCurrentUserType() == "shelterOwner"){
            binding.btnPetDetailsAdopt.visibility = View.GONE
            binding.ivPetDetailsSharePet.visibility = View.GONE
            binding.ivPetDetailsLikePet.visibility = View.GONE

        }

        viewPager = binding.vpPetDetailsImage

        shelterId = args.shelterId
        petId = args.petId

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        // Changed to DAO
        SheltersDAO().getShelterById(shelterId).addOnSuccessListener { shelterDataSnapshot ->
            if(shelterDataSnapshot.getValue() != null){

                shelter = shelterDataSnapshot.getValue(Shelter::class.java)

                shelter?.let { s ->

                    // Changed to DAO
                    PetDAO().getPet(petId, shelterId).addOnSuccessListener { petDataSnapshot ->
                            if(petDataSnapshot.getValue() != null){
                                pet = petDataSnapshot.getValue<ShelterPet>()
                                pet?.imageURL?.let{
                                    viewPagerAdapter = PetDetailImageCorousalAdapter(requireContext(), it)
                                    viewPager.adapter = viewPagerAdapter
                                    indicator = requireView().findViewById(R.id.inPetDetailsImage) as CircleIndicator
                                    indicator.setViewPager(viewPager)
                                }

                                binding.apply {
                                    tvPetDetailsPetName.text = pet?.name
                                    tvPetDetailsPetBreed.text = pet?.breed
                                    tvPetDetailsPetAge.text = pet?.age.toString() + " years old"
                                    tvPetDetailsPetDescription.text = pet?.description
                                    tvPetDetailsShelterName.text = shelter?.name
                                    tvPetDetailsShelterAddress.text = shelter?.address
                                    tvPetDetailsShelterDescription.text = shelter?.description
                                }


                                val mapFragment =
                                    childFragmentManager.findFragmentById(R.id.mapPetDetailsShelterLocation) as SupportMapFragment?
                                mapFragment?.getMapAsync(callback)

                            }
                        }
                }
            }
        }


        PendingAdoptionDAO().getAdoptionList(shelterId).observe(viewLifecycleOwner){ snapShot ->
            if(snapShot!=null)
            for ((key, value) in snapShot ) {

                if (value.userId.equals(userId) && value.petId.equals(petId)) {
                    binding.btnPetDetailsAdopt.isEnabled = false
                }
            }

        }




        // Changed to DAO
        UserLovedPetDAO().getUserLovedPets(userId).observe(viewLifecycleOwner){

            if (!it.isNullOrEmpty()) {
                for ((key, lovedPet) in it) {
                    if (lovedPet?.shelterId!!.equals(shelterId) && lovedPet?.petId.equals(petId)) {
                        liked = lovedPet?.id
                        binding.ivPetDetailsLikePet.setImageResource(R.drawable.ic_round_love_24)
                    }
                }

                if (liked.isNullOrBlank()) {
                    binding.ivPetDetailsLikePet.setImageResource(R.drawable.ic_round_love_black_24)
                }
            }
        }




        val userLovedPetDAO: UserLovedPetDAO = UserLovedPetDAO()

        binding.ivPetDetailsLikePet.setOnClickListener {
            if (liked.isNullOrBlank()) {
                val userLovedPet: UserLovedPet =
                    UserLovedPet(UUID.randomUUID().toString(), petId, shelterId)

                // Changed to DAO
                userLovedPetDAO.addLovedPet(userId, userLovedPet)
                liked = userLovedPet.id
                binding.ivPetDetailsLikePet.setImageResource(R.drawable.ic_round_love_24)
            } else {

                // Changed to DAO
                userLovedPetDAO.deleteLovedPet(userId, liked!!)
                binding.ivPetDetailsLikePet.setImageResource(R.drawable.ic_round_love_black_24)
                liked = null;
            }
        }

        binding.ivPetDetailsSharePet.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I found a pet!")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            requireContext().startActivity(shareIntent)
        }



        binding.btnPetDetailsAdopt.setOnClickListener {

            val action = PetDetailFragmentDirections.actionPetDetailFragmentToAdoptPetFragment(petId, shelterId)

            findNavController().navigate(action)
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->

        val latLng = LatLng(shelter?.latitude!!, shelter?.longitude!!)
        val markerOptions = MarkerOptions().position(latLng).title("Shelter")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        googleMap.addMarker(markerOptions)
    }

}
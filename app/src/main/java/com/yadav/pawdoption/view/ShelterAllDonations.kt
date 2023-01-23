package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.ShelterAllDonationsAdapter
import com.yadav.pawdoption.adapter.UserDonationsAdapter
import com.yadav.pawdoption.model.ShelterDonation
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.SheltersDAO
import com.yadav.pawdoption.persistence.UsersDAO


class ShelterAllDonations : Fragment() {

    var currentUser: String = FirebaseDatabaseSingleton.getCurrentUid()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var shelterAllDonationsAdapter: ShelterAllDonationsAdapter
    private val shelterDAO = SheltersDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_shelter_all_donations, container, false)

        activity?.title = "All Donations"
        shelterAllDonationsAdapter = ShelterAllDonationsAdapter(requireContext(), arrayListOf())
        linearLayoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvShelterAllDonations)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = shelterAllDonationsAdapter

        val donations : ArrayList<ShelterDonation> = arrayListOf()

        shelterDAO.getShelters().observe(viewLifecycleOwner){
            var currentUserDonations = it.get(currentUser)!!.donations

            if(currentUserDonations != null){
                var donationKeys = currentUserDonations.keys
                for(dk in donationKeys){
                    donations.add(currentUserDonations.get(dk)!!)
                }
                shelterAllDonationsAdapter = ShelterAllDonationsAdapter(requireContext(),donations)
                recyclerView.adapter = shelterAllDonationsAdapter
                shelterAllDonationsAdapter.notifyDataSetChanged()
            } else {
                shelterAllDonationsAdapter = ShelterAllDonationsAdapter(requireContext(),
                    arrayListOf()
                )
                recyclerView.adapter = shelterAllDonationsAdapter
                shelterAllDonationsAdapter.notifyDataSetChanged()
            }
        }




        return view
    }


}
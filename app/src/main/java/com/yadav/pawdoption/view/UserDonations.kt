package com.yadav.pawdoption.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.UserDonationsAdapter
import com.yadav.pawdoption.model.User
import com.yadav.pawdoption.model.UserDonation
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.UsersDAO


class UserDonations : Fragment() {

    var currentUser: String = FirebaseDatabaseSingleton.getCurrentUid()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userDonationsAdapter: UserDonationsAdapter
    private val userDAO = UsersDAO()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_donations, container, false)

        activity?.title = "My Donations"

        userDonationsAdapter = UserDonationsAdapter(requireContext(), arrayListOf())
        linearLayoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvUserDonation)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = userDonationsAdapter

        var donations: ArrayList<UserDonation> = arrayListOf()

        userDAO.getUserList().observe(viewLifecycleOwner) {

            var currentUserDonations = it.get(currentUser)!!.donations

            if(currentUserDonations != null){
                var donationKeys = currentUserDonations.keys
                for(dk in donationKeys){
                    donations.add(currentUserDonations.get(dk)!!)
                }
                userDonationsAdapter = UserDonationsAdapter(requireContext(), donations)
                recyclerView.adapter = userDonationsAdapter
                userDonationsAdapter.notifyDataSetChanged()
            } else {
                userDonationsAdapter = UserDonationsAdapter(requireContext(), arrayListOf())
                recyclerView.adapter = userDonationsAdapter
                userDonationsAdapter.notifyDataSetChanged()
            }


        }




        return view
    }


}
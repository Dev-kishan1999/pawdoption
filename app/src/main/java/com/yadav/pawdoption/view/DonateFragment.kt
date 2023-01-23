package com.yadav.pawdoption.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.ShelterDonation
import com.yadav.pawdoption.model.UserDonation
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import java.time.LocalDateTime
import java.util.*


class DonateFragment : Fragment() {

    val args: DonateFragmentArgs by navArgs()
    lateinit var tvShelterTitle: TextView
    lateinit var tvShelterDescription: TextView
    lateinit var bDonate: Button
    var currentUser: String = FirebaseDatabaseSingleton.getCurrentUid()
    lateinit var shelter: Shelter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_donate, container, false)
        shelter = args.shelter

        tvShelterTitle = view.findViewById(R.id.tvShelterTitle)
        tvShelterDescription = view.findViewById(R.id.tvShelterDescription)
        bDonate = view.findViewById(R.id.payButton)



        tvShelterTitle.text = shelter.name?.uppercase()
        tvShelterDescription.text = shelter.description
        bDonate.setOnClickListener {
            Toast.makeText(
                activity?.applicationContext,
                "Thank you for you donation.",
                Toast.LENGTH_LONG
            ).show()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bDonate.setOnClickListener {
            var userDonation: UserDonation = UserDonation(
                UUID.randomUUID().toString(), shelter.id,
                view.findViewById<TextInputEditText>(R.id.ttComment).text.toString(),
                view.findViewById<TextInputEditText>(R.id.ttAmount).text.toString().toDouble(),
                LocalDateTime.now().toString()
            )

            var shelterDonation: ShelterDonation = ShelterDonation(
                userDonation.id, currentUser,
                view.findViewById<TextInputEditText>(R.id.ttComment).text.toString(),
                view.findViewById<TextInputEditText>(R.id.ttAmount).text.toString().toDouble(),
                LocalDateTime.now().toString()
            )

            FirebaseDatabaseSingleton.getUsersReference().child(currentUser).child("donations")
                .child(userDonation.id!!).setValue(userDonation)

            FirebaseDatabaseSingleton.getSheltersReference().child(shelter.id!!).child("donations")
                .child(userDonation.id!!).setValue(shelterDonation)


            findNavController().navigate(R.id.userDonations)

        }

    }

}
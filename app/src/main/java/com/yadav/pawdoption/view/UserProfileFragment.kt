package com.yadav.pawdoption.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.yadav.pawdoption.R
import com.yadav.pawdoption.databinding.FragmentUserProfileBinding
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.User
import com.yadav.pawdoption.persistence.UsersDAO

//https://firebase.google.com/docs/database

class UserProfileFragment : Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var _binding: FragmentUserProfileBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var userType = ""
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        activity?.title = "Profile"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Linear layout for petAdopter and shelterOwner

        var linearLayout1 = view.findViewById(R.id.linearLayout1) as LinearLayout
        var linearLayout2 = view.findViewById(R.id.linearLayout2) as LinearLayout


        //To trigger sign out from application
        binding.logoutBtn.setOnClickListener {

            bottomNavigationView = if (userType == "petAdopter")
                activity?.findViewById(R.id.bottom_nav_pet_owner)!!
            else
                activity?.findViewById(R.id.bottom_nav_shelter)!!
            bottomNavigationView.visibility = View.GONE

            firebaseAuth.signOut()

            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.loginFragment)
        }


        // get current user id and email

        firebaseAuth = FirebaseAuth.getInstance()
        var uid = firebaseAuth.currentUser?.uid

        auth = Firebase.auth
        firebaseAuth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("UserType")
            databaseReference.child(uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val shelter = dataSnapshot.getValue()
                        if (shelter == null) {
                            Log.e("User", "User data is null")
                            return
                        }
                        userType = shelter.toString()
                        // setting user details from database using current user i.e Pet owner
                        if (userType == "petAdopter") {
                            linearLayout1.visibility = View.VISIBLE
                            linearLayout2.visibility = View.GONE
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users")

                            databaseReference!!.child(uid!!)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val user = dataSnapshot.getValue(User::class.java)

                                        if (user == null) {
                                            Log.e("User", "User data is null")
                                            return
                                        }
                                        Log.e("user", "User data is changed!" + user.name)
                                        binding.userEmail.setText(currentUser.email.toString())
                                        binding.userName.setText(user.name)
                                        binding.userAddress.setText(user.address)



                                        binding.viewMyDonations.setOnClickListener() {
                                            if (user.donations?.isEmpty() == true) {
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "You have not made any donations",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Navigation.findNavController(
                                                    requireActivity(),
                                                    R.id.nav_host_fragment
                                                )
                                                    .navigate(R.id.userDonations)
                                            }

                                        }



                                        binding.viewMyAppointments.setOnClickListener() {
                                            if (user.appointments == null) {
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "You have not made any appointments",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Navigation.findNavController(
                                                    requireActivity(),
                                                    R.id.nav_host_fragment
                                                )
                                                    .navigate(R.id.myAppointment)
                                            }
                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        //Failed to read value
                                        Log.e("User", "Failed to read user", error.toException())
                                    }
                                })
                        } else {

                            // setting user details from database using current user i.e Pet owner
                            //change visibility of shelterOwner to avaialable

                            linearLayout1.visibility = View.GONE
                            linearLayout2.visibility = View.VISIBLE
                            databaseReference =
                                FirebaseDatabase.getInstance().getReference("Shelters")
                            databaseReference!!.child(uid!!)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val shelter = dataSnapshot.getValue(Shelter::class.java)


                                        if (shelter == null) {
                                            Log.e("User", "User data is null")
                                            return
                                        }
                                        Log.e("user", "User data is changed!" + shelter.name)
                                        binding.shelterEmail.setText(currentUser.email.toString())
                                        binding.shelterName.setText(shelter.name)
                                        binding.shelterAddress.setText(shelter.address)
                                        binding.shelterDescription.setText(shelter.description)

                                        binding.viewDonations.setOnClickListener() {

                                            if (shelter.donations.isEmpty()) {
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "Password not matching",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Navigation.findNavController(
                                                    requireActivity(),
                                                    R.id.nav_host_fragment
                                                )
                                                    .navigate(R.id.shelterAllDonations)
                                            }
                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("User", "Failed to read user", error.toException())
                                    }
                                })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //Failed to read value
                        Log.e("User", "Failed to read user", error.toException())
                    }
                })


        }


    }


}
package com.yadav.pawdoption

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yadav.pawdoption.databinding.ActivityMainBinding
import com.yadav.pawdoption.databinding.FragmentUserProfileBinding
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.UsersDAO

// https://guides.codepath.com/android/Bottom-Navigation-Views
// https://stackoverflow.com/questions/53902494/navigation-component-cannot-find-navcontroller
// https://firebase.google.com/docs/auth/android/start#check_current_auth_state
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val usersDAO = UsersDAO()
    private lateinit var _binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Check if user is signed in (non-null) and update UI accordingly.
    public override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            FirebaseDatabaseSingleton.setCurrentUid(currentUser.uid)
            usersDAO.setCurrentUserTypeByUid(currentUser.uid)
            Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigate(R.id.petListFragment)
            usersDAO.getCurrentUserTypeByUid().observe(this) {
                setBottomNavigation(it)
            }
        }
        else{
            Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigate(R.id.loginFragment)
        }
    }

//    Setup bottom navigation bar as per logged in user type
    fun setBottomNavigation(userType: String) {
        bottomNavigationView = if (userType == "petAdopter")
            findViewById(R.id.bottom_nav_pet_owner)
        else
            findViewById(R.id.bottom_nav_shelter)
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.selectedItemId = R.id.pets
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pets -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.petListFragment)
                    true
                }
                R.id.shelters -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.mapsFragment)
                    true
                }
                R.id.vet -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.bookAppointment)
                    true
                }
                R.id.profile -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.userProfileFragment)
                    true
                }

                R.id.pending -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.pendingAdoptionFragment)
                    true
                }
                R.id.donations -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.shelterAllDonations)
                    true
                }
                R.id.loved -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.userDonations)
                    true
                }
                else -> true
            }

        }
    }


}
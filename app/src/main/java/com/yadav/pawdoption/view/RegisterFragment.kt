package com.yadav.pawdoption.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hbb20.CountryCodePicker
import com.yadav.pawdoption.R
import com.yadav.pawdoption.databinding.FragmentRegisterBinding
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.User
import com.yadav.pawdoption.persistence.SheltersDAO
import com.yadav.pawdoption.persistence.UsersDAO
import kotlinx.android.synthetic.main.fragment_register.*

//https://firebase.google.com/docs/database
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    private lateinit var pass: String

    private val usersDAO = UsersDAO()
    private val sheltersDAO = SheltersDAO()
    var ccp: CountryCodePicker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.redirectLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val radioGroup = view.findViewById(R.id.radio) as RadioGroup
        var linearLayout1 = view.findViewById(R.id.linearLayout1) as LinearLayout
        var linearLayout2 = view.findViewById(R.id.linearLayout2) as LinearLayout
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

                if (shelter_btn.isChecked) {
                    linearLayout1.visibility = View.GONE
                    linearLayout2.visibility = View.VISIBLE
                } else {
                    linearLayout1.visibility = View.VISIBLE
                    linearLayout2.visibility = View.GONE
                }
            }
        })




        firebaseAuth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener() {

            //get details from user input

            if (linearLayout1.visibility == View.VISIBLE) {  //pet owner
                val email = binding.emailEt.text.toString()
                var pass = binding.passwordEt.text.toString()
                // var pass = " "

                val Name = binding.firstNameEt.text.toString() + " " + binding.lastNameEt.text.toString()
                val phoNumber = binding.phoneNumberEt.text.toString()
                val address1 = binding.addressEt.text.toString()
                val address2 = binding.address2Et.text.toString()
                val confirmPasswor = binding.repasswordEt.text.toString()
                val address = address1 + " " + address2
                val mUserType = "petAdopter"
                var flag = 0

                //validation for user input from registration

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Toast.makeText(this.context, "Invalid email address", Toast.LENGTH_SHORT).show()
                    flag = 1
                }
                if (pass.length < 6 || confirmPasswor.length < 6) {
                    password.helperText = "Password length small"
                    flag = 1

                }

                if (!confirmPasswor.matches(".*[A-Z].*".toRegex())) {
                    password.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }

                if (!confirmPasswor.matches(".*[a-z].*".toRegex())) {
                    password.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }
                if (!confirmPasswor.matches(".*[@#\$%^&+=].*".toRegex())) {
                    password.helperText = "Must contain 1 special character"
                    flag = 1
                }


                if (!pass.matches(".*[A-Z].*".toRegex())) {
                    password.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }

                if (!pass.matches(".*[a-z].*".toRegex())) {
                    password.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }
                if (!pass.matches(".*[@#\$%^&+=].*".toRegex())) {
                    password.helperText = "Must contain 1 special character"
                    flag = 1
                }

                if (Name.isEmpty()) {
                    firstName.helperText = "Cannot be Empty"
                    lastName.helperText = "Cannot be Empty"
                    flag = 1
                }


                if (address1.isEmpty() || address2.isEmpty()) {
                    binding.address.helperText = "Please fill address fields correctly"
                    address_2.helperText = "Please fill address fields correctly"
                    flag = 1
                }




                if (email.isNotEmpty() && pass.isNotEmpty() && confirmPasswor.isNotEmpty() && flag != 1) {

                    if (pass == confirmPasswor) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {

                                    val uid = firebaseAuth.currentUser?.uid
                                    databaseReference =
                                        FirebaseDatabase.getInstance().getReference("Users")
//if user is registered.. store details into realtime database
                                    if (uid != null) {
                                        val user =
                                            User(uid.toString(), Name, address, email, phoNumber)
                                        databaseReference.child(uid).setValue(user)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    //  databaseReference.child(uid).setValue(userType)
                                                    Toast.makeText(
                                                        this.context,
                                                        "Registered",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        this.context,
                                                        "was not able to create profile",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        databaseReference =
                                            FirebaseDatabase.getInstance().getReference("UserType")

                                        databaseReference.child(uid).setValue(mUserType)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    //  databaseReference.child(uid).setValue(userType)
                                                    Log.d(
                                                        mUserType,
                                                        "User Type succesfully added "
                                                    );
                                                } else {
                                                    Log.d(
                                                        mUserType,
                                                        "User Type not succesfully added "
                                                    );
                                                }

                                            }
                                    }
                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                } else {
                                    Toast.makeText(
                                        this.context,
                                        it.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this.context, "Password not matching", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this.context, "Empty field is not allowed", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                val email = binding.shelterEmailEt.text.toString()
                val pass = binding.shelterPasswordEt.text.toString()
                val confirmPasswor = binding.shelterRepasswordEt.text.toString()
                var flag = 0
                val Name = binding.shelterNameEt.text.toString()
                val phoNumber = binding.phoneNumberEt.text.toString()
                val address1 = binding.shelterAddressEt.text.toString()
                val address2 = binding.shelterAddress2Et.text.toString()
                val shelterDesc = binding.shelterDescriptionEt.text.toString()
                val userType = "shelterOwner"

                //validation for shelter owner
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Toast.makeText(this.context, "Invalid email address", Toast.LENGTH_SHORT).show()
                    flag = 1
                }
                if (pass.length < 6) {
                    shelterPassword.helperText = "Password length small"
                    flag = 1

                }

                if (!pass.matches(".*[A-Z].*".toRegex())) {
                    shelterPassword.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }

                if (!pass.matches(".*[a-z].*".toRegex())) {
                    shelterPassword.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }
                if (!pass.matches(".*[@#\$%^&+=].*".toRegex())) {
                    shelterPassword.helperText = "Must contain 1 special character"
                    flag = 1
                }
                if (!confirmPasswor.matches(".*[A-Z].*".toRegex())) {
                    shelterRePassword.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }

                if (!confirmPasswor.matches(".*[a-z].*".toRegex())) {
                    shelterRePassword.helperText = "Must contain 1 Upper case charcter"
                    flag = 1
                }
                if (!confirmPasswor.matches(".*[@#\$%^&+=].*".toRegex())) {
                    shelterRePassword.helperText = "Must contain 1 special character"
                    flag = 1
                }

                if (Name.isEmpty()) {
                    shelterName.helperText = "Cannot be empty"
                    flag = 1
                }

                if (shelterDesc.isEmpty()) {
                    shelter_description.helperText = "Do not leave this empty"
                    flag = 1
                }
                if (address1.isEmpty() || address2.isEmpty()) {
                    shelter_address.helperText = "Please fill address fields correctly"
                    shelter_address2.helperText = "Please fill address fields correctly"
                    flag = 1
                }

                if (email.isNotEmpty() && pass.isNotEmpty() && confirmPasswor.isNotEmpty() && flag != 1) {

                    if (pass == confirmPasswor) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {

                                    val uid = firebaseAuth.currentUser?.uid
                                    databaseReference =
                                        FirebaseDatabase.getInstance().getReference("Shelters")

                                    if (uid != null) {
                                        val shelter = Shelter(
                                            uid.toString(),
                                            Name,
                                            shelterDesc,
                                            address2,
                                            null,
                                            null
                                        )

                                        shelter.latitude = 44.650111
                                        shelter.longitude = -63.59525

                                        databaseReference.child(uid).setValue(shelter)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    Toast.makeText(
                                                        this.context,
                                                        "Registered",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        this.context,
                                                        "was not able to create profile",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        databaseReference =
                                            FirebaseDatabase.getInstance().getReference("UserType")

                                        databaseReference.child(uid).setValue(userType)
                                            .addOnCompleteListener {
                                                if (it.isSuccessful) {

                                                    Log.d(userType, "User Type succesfully added ");
                                                } else {
                                                    Log.d(
                                                        userType,
                                                        "User Type not succesfully added "
                                                    );
                                                }

                                            }
                                    }
                                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                } else {
                                    Toast.makeText(
                                        this.context,
                                        it.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this.context, "Password not matching", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this.context, "Empty field is not allowed", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }


    }


}
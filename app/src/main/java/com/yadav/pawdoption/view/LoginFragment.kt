package com.yadav.pawdoption.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yadav.pawdoption.MainActivity
import com.yadav.pawdoption.R
import com.yadav.pawdoption.databinding.FragmentLoginBinding
import com.yadav.pawdoption.databinding.FragmentRegisterBinding
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import kotlinx.android.synthetic.main.fragment_register.*

//https://firebase.google.com/docs/database

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var databaseReference: DatabaseReference

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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.redirectRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //procedure to login to the Pawadoption application
        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmailEt.text.toString()
            val pass = binding.loginPasswordEt.text.toString()

            firebaseAuth = FirebaseAuth.getInstance()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                //firebase authentication with email and password
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this.context, "Login Successful", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_petListFragment2)
                    } else {
                        Toast.makeText(this.context, "Could not log in. Please try again!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this.context, "Empty field is not allowed", Toast.LENGTH_SHORT)
                    .show()
            }

        }



        binding.forgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }


    }




}


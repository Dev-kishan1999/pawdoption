package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.yadav.pawdoption.R
import com.yadav.pawdoption.databinding.FragmentForgetPasswordBinding
import com.yadav.pawdoption.databinding.FragmentLoginBinding

//https://firebase.google.com/docs/database
class ForgetPasswordFragment : Fragment() {
    private var _binding: FragmentForgetPasswordBinding? = null

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }


    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBackButton.setOnClickListener{
            findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
        }
        binding.forgetPasswordBtn.setOnClickListener {
            val email: String = binding.forgetPasswordEmailEt.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                Toast.makeText(this.context, "Please enter email address", Toast.LENGTH_SHORT)
                    .show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this.context,
                                "Continue to your email to rest your password",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                        } else {
                            Toast.makeText(this.context,"Please try again",Toast.LENGTH_SHORT).show()
                            binding.forgetPasswordEmailEt.setText(" ")
                        }
                    }
            }

        }


    }
}
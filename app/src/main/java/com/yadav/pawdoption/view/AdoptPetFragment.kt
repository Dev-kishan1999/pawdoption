//https://stackoverflow.com/questions/3149414/how-to-receive-a-event-on-android-checkbox-check-change
package com.yadav.pawdoption.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.yadav.pawdoption.R
import com.yadav.pawdoption.databinding.FragmentAdoptPetBinding
import com.yadav.pawdoption.databinding.FragmentConfirmAdoptionBinding
import com.yadav.pawdoption.model.PendingAdoption
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.PendingAdoptionDAO
import com.yadav.pawdoption.persistence.SheltersDAO
import java.time.LocalDateTime
import java.util.*


class AdoptPetFragment : Fragment() {

    var checkBox: CheckBox? = null;

    var _binding: FragmentAdoptPetBinding? = null

    private val binding get() = _binding!!

    val args: AdoptPetFragmentArgs by navArgs()

    lateinit var shelterId: String

    lateinit var petId: String

    lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        activity?.title = "Adopt Pet "

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: "uid1"

        shelterId = args.shelterId
        petId = args.petId

        _binding = FragmentAdoptPetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cbAgreeTnC.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.btnAdoptPetSubmit.isEnabled = isChecked
        }

        binding.btnAdoptPetSubmit.setOnClickListener {

            // Changed to DAO
            SheltersDAO().getShelterById(shelterId).addOnSuccessListener {

                val pendingAdoption: PendingAdoption = PendingAdoption(UUID.randomUUID().toString(),userId, args.petId, LocalDateTime.now().toString())

                // Changed to DAO

                PendingAdoptionDAO().addPendingAdoption(shelterId, pendingAdoption)

                binding.btnAdoptPetSubmit.isEnabled = false

                val myToast = Toast.makeText(requireContext(),"Your request adoption request has been created successfully, You will be notified about update on request", Toast.LENGTH_SHORT)

                myToast.setGravity(Gravity.LEFT,200,200)
                myToast.show()

                findNavController().navigate(R.id.action_adoptPetFragment_to_petListFragment)
            }
        }

    }
}
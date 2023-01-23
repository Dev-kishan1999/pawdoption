package com.yadav.pawdoption.view

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.yadav.pawdoption.R
import com.yadav.pawdoption.databinding.FragmentConfirmAdoptionBinding
import com.yadav.pawdoption.model.PendingAdoption
import com.yadav.pawdoption.model.ShelterPet
import com.yadav.pawdoption.model.User
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.PendingAdoptionDAO
import com.yadav.pawdoption.persistence.UsersDAO
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class ConfirmAdoptionFragment : Fragment() {

    var _binding: FragmentConfirmAdoptionBinding? = null

    var pendingAdoption: PendingAdoption? = null;
    var shelterPet: ShelterPet? = null;
    var user: User? = null;
    var image: String? = null;

    var shelterId: String = FirebaseDatabaseSingleton.getCurrentUid()

    val args: ConfirmAdoptionFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        https://developer.android.com/guide/navigation/navigation-pass-data

        if(shelterId == null){
            shelterId = "2001"
        }
        activity?.title = "Confirm pending adoption "

        pendingAdoption = args.pendingAdoption
        shelterPet = args.shelterPet
        user = args.user

        image = args.petImage

        _binding = FragmentConfirmAdoptionBinding.inflate(inflater, container, false)

        binding.apply {
            Picasso.with(requireContext()).load(image).into(ivConfirmAdoptionPetImage)
            tvConfirmAdoptionPetName.text = shelterPet?.name
            tvConfirmAdoptionPetBreed.text = shelterPet?.breed
            tvConfirmAdoptionPetAge.text = shelterPet?.age.toString() + " years old"
            tvConfirmAdoptionPetDescription.text = shelterPet?.description

            tvConfirmAdoptionAdopterName.text = user?.name
            tvConfirmAdoptionAdopterMobileNumber.text = user?.mobileNumber
            tvConfirmAdoptionAdopterAddress.text = user?.address
        }

        return binding.root
//        return inflater.inflate(R.layout.fragment_confirm_adoption, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnConfirmAdopterApprove.setOnClickListener {
            val SDK_INT = Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)

                run(user?.email!!, "Request Approval", "Hi, " +
                    "Your request for the pet adoption is approved." +
                    "You can visit the shelter and complete the remaining procedure." +
                    "Thanks")
            }


        }

        binding.btnConfirmAdoptionDeny.setOnClickListener {

            val SDK_INT = Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)
                run(user?.email!!, "Request denied", "Hi, " +
                        "Your request for the pet adoption is denied." +
                        "You can create a request again" +
                        "Thanks")
            }




        }

    }



//    https://square.github.io/okhttp/recipes/
//    https://stackoverflow.com/questions/45219379/how-to-make-an-api-request-in-kotlin

    fun run(email: String, subject: String, body: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://ak4t4m6mngdpkuzv4f2rycjihi0opvik.lambda-url.us-east-1.on.aws/?receiver=" + email + "&subject=" + subject + "&body=" + body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")


            binding.btnConfirmAdopterApprove.isEnabled = false
            binding.btnConfirmAdoptionDeny.isEnabled = false

            // Changed to DAO
            PendingAdoptionDAO().deletePendingAdoption(pendingAdoption?.id!!, shelterId);

            val approveToast = Toast.makeText(requireContext(), "Email sent ", Toast.LENGTH_SHORT)
            approveToast.setGravity(Gravity.LEFT,200,200)
            approveToast.show()

            findNavController().navigate(R.id.action_confirmAdoptionFragment_to_pendingAdoptionFragment)
        }
    }


}
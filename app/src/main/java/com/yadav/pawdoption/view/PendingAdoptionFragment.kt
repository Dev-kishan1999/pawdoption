package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.adapter.PendingAdoptionViewAdapter
import com.yadav.pawdoption.databinding.FragmentPendingAdoptionsBinding
import com.yadav.pawdoption.model.PendingAdoptionData
import com.yadav.pawdoption.model.ShelterPet
import com.yadav.pawdoption.model.User
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.PendingAdoptionDAO
import com.yadav.pawdoption.persistence.PetDAO
import com.yadav.pawdoption.persistence.UsersDAO

class PendingAdoptionFragment : Fragment() {

    var _binding: FragmentPendingAdoptionsBinding? = null

    private val binding get() = _binding!!

    var shelterID: String = FirebaseDatabaseSingleton.getCurrentUid()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pending_adoptions, container, false)

        if(shelterID == null){
            shelterID = "2001"
        }

        activity?.title = "Pending Adoption List"

        _binding = FragmentPendingAdoptionsBinding.inflate(inflater, container, false)


        var pendingAdoptionAdapter: RecyclerView.Adapter<PendingAdoptionViewAdapter.ViewHolder> = PendingAdoptionViewAdapter(requireContext(),
            mutableListOf());

        _binding?.apply {
            rvPendingAdoptions.apply{
                layoutManager= LinearLayoutManager(requireActivity())
                adapter=pendingAdoptionAdapter
            }
        }

        val pendingAdoptionDAO = PendingAdoptionDAO();


        val mld = pendingAdoptionDAO.getAdoptionList(shelterID)

        mld.observe(viewLifecycleOwner) {

            if(it != null) {
                val paList: MutableList<PendingAdoptionData> = mutableListOf()
                for ((key, value) in it) {

                    var pet: ShelterPet? = null;

                        // Changed to the DAO
                        PetDAO().getPet(value.petId!!, shelterID).addOnSuccessListener { p ->
                            pet = p.getValue(ShelterPet::class.java)

                            var user: User? = null

                            // Changed to DAO
                            UsersDAO().getUserById(value.userId!!).addOnSuccessListener {
                                user = it.getValue(User::class.java)
                                val pendingAdoptionData: PendingAdoptionData =
                                    PendingAdoptionData(value, pet, user)

                                paList.add(pendingAdoptionData)

                                pendingAdoptionAdapter =
                                    PendingAdoptionViewAdapter(requireContext(), paList)
                                binding.rvPendingAdoptions.adapter = pendingAdoptionAdapter
                                pendingAdoptionAdapter.notifyDataSetChanged()
                            }
                        }

                }
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
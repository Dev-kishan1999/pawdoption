package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.yadav.pawdoption.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yadav.pawdoption.model.Veterinarian
import com.yadav.pawdoption.persistence.AppointmentDAO
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton


class CreateVetAppointment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_vet_appointment, container, false)

        activity?.title = "Create Schedule"

        val btnCreateSchedule = view.findViewById<Button>(R.id.btnCreateSchedule)
        btnCreateSchedule.setOnClickListener {
            // Name reference
            val tilVetName = view.findViewById<TextInputLayout>(R.id.tilVetName)
            val tiVetName = view.findViewById<TextInputEditText>(R.id.tiVetName)

            // Qualification reference
            val tilVetQualification = view.findViewById<TextInputLayout>(R.id.tilVetQualification)
            val tiVetQualification = view.findViewById<TextInputEditText>(R.id.tiVetQualification)

            // Error text reference
            val tvErrorDays = view.findViewById<TextView>(R.id.tvErrorDays)

            // Days chip group reference
            val cgDays: ChipGroup = view.findViewById(R.id.cgDays)

            // Validate
            val vetName = tiVetName.text.toString()
            val vetQualification = tiVetQualification.text.toString()

            if (!isValid(vetName, tilVetName, vetQualification, tilVetQualification, tvErrorDays, cgDays)) {
                return@setOnClickListener
            }

            // Call firebase
            val appointmentDAO = AppointmentDAO()

            val days = arrayListOf<String>()
            for (i in cgDays.checkedChipIds) {
                val chip = view.findViewById<Chip>(i)
                if (chip.isChecked) {
                    days.add(chip.text.toString())
                }
            }

            val vet = Veterinarian(
                name = vetName,
                qualification = vetQualification,
                days = days
            )
            appointmentDAO.createSchedule(FirebaseDatabaseSingleton.getCurrentUid(), vet)
            Toast.makeText(requireContext(), "Successfully added vet", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_createVetAppointment_to_bookAppointment)
        }

        return view
    }

    private fun isValid(vetName: String, tilVetName: TextInputLayout,
                        vetQualification: String, tilVetQualification: TextInputLayout,
                        tvErrorDays: TextView, cgDays: ChipGroup): Boolean {
        var isValid = true

        if (vetName.equals("")) {
            tilVetName.setError("Veterinarian's name cannot be empty")
            isValid = false
        } else {
            tilVetName.setError(null)
        }

        if (vetQualification.equals("")) {
            tilVetQualification.setError("Qualification cannot be empty")
            isValid = false
        } else {
            tilVetQualification.setError(null)
        }

        if (cgDays.checkedChipIds.size == 0) {
            tvErrorDays.text = "Select at least one day"
            isValid = false
        } else {
            tvErrorDays.text = ""
        }

        return isValid
    }

}
package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.MyAppointmentAdapter
import com.yadav.pawdoption.model.Appointment
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.UsersDAO


class MyAppointment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var myAppointmentAdapter: MyAppointmentAdapter
    private val userssDAO = UsersDAO()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_appointment, container, false)

        activity?.title = "My Appointment"

        setupRecyclerView(view)

        return view
    }

    private fun setupRecyclerView(view: View) {
        myAppointmentAdapter = MyAppointmentAdapter(mutableListOf())
        linearLayoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvMyAppointments)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = myAppointmentAdapter
        userssDAO.getUserList().observe(
            viewLifecycleOwner
        ) {
            val appointments = it.get(FirebaseDatabaseSingleton.getCurrentUid())?.appointments

            if(appointments != null){
                myAppointmentAdapter = MyAppointmentAdapter(appointments as List<Appointment>)
                recyclerView.adapter = myAppointmentAdapter
                myAppointmentAdapter.notifyDataSetChanged()
            } else {
                myAppointmentAdapter = MyAppointmentAdapter(mutableListOf())
                recyclerView.adapter = myAppointmentAdapter
                myAppointmentAdapter.notifyDataSetChanged()
            }

        }
    }
}
package com.yadav.pawdoption.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.yadav.pawdoption.R
import com.yadav.pawdoption.adapter.DynamicAppointmentsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton


class BookAppointment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_appointment, container, false)

        activity?.title = "Book Appointment"

        val tlDays = view.findViewById<TabLayout>(R.id.tlDays)
        tlDays!!.addTab(tlDays!!.newTab().setText("Monday"))
        tlDays!!.addTab(tlDays!!.newTab().setText("Tuesday"))
        tlDays!!.addTab(tlDays!!.newTab().setText("Wednesday"))
        tlDays!!.addTab(tlDays!!.newTab().setText("Thursday"))
        tlDays!!.addTab(tlDays!!.newTab().setText("Friday"))
        tlDays.setTabGravity(TabLayout.GRAVITY_FILL);

        val vpAppointments = view.findViewById<ViewPager>(R.id.vpAppointments)

        val adapter = DynamicAppointmentsAdapter(requireContext(), parentFragmentManager, tlDays!!.tabCount)
        vpAppointments!!.adapter = adapter

        vpAppointments!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tlDays))

        tlDays!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vpAppointments!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        /** Get reference of the FAB **/
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)
        if (FirebaseDatabaseSingleton.getCurrentUserType().uppercase().equals("PETADOPTER"))
            fabAdd.visibility = View.GONE
        else {
            fabAdd.visibility = View.VISIBLE
            /** Navigate to the second fragment on clicking on the next button **/
            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_bookAppointment_to_createVetAppointment)
            }
        }

        return view
    }

}
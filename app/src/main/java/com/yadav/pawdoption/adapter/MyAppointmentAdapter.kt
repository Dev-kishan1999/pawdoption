package com.yadav.pawdoption.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.model.MyAppointment
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.Appointment

/** Adapter class for notes containing logic for manipulating my appointment in the recycler view */
class MyAppointmentAdapter(
    private var myAppointments: List<Appointment>
) : RecyclerView.Adapter<MyAppointmentAdapter.MyAppointmentViewHolder>() {

    /** Inner view holder class for note **/
    inner class MyAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /** Assigning note title and body **/
    override fun onBindViewHolder(holder: MyAppointmentViewHolder, position: Int) {
        val maTvShelterDoctor = holder.itemView.findViewById<TextView>(R.id.maTvShelterDoctor)
        val maTvQualification = holder.itemView.findViewById<TextView>(R.id.maTvQualification)
        val maTvDate = holder.itemView.findViewById<TextView>(R.id.maTvDate)
        val maTvTime = holder.itemView.findViewById<TextView>(R.id.maTvTime)

        holder.itemView.apply {
            maTvShelterDoctor.text = "${myAppointments[position].shelterName} - ${myAppointments[position].vetName}"
            maTvQualification.text = myAppointments[position].vetQualification
            maTvDate.text = myAppointments[position].day
            maTvTime.text = myAppointments[position].time
        }
    }

    /** Inflate the my appointment layout **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_appointment_item, parent, false)
        return MyAppointmentViewHolder(view)
    }

    /** Get notes count **/
    override fun getItemCount(): Int {
        return myAppointments.size
    }
}
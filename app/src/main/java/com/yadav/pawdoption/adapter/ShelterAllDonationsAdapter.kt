package com.yadav.pawdoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.ShelterDonation
import com.yadav.pawdoption.model.UserDonation

class ShelterAllDonationsAdapter(private val context: Context, private val shelterDonations: ArrayList<ShelterDonation>) :
    RecyclerView.Adapter<ShelterAllDonationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvsDonateAmount: TextView
        val tvsUser: TextView


        init {
            tvsDonateAmount = view.findViewById(R.id.tvsDonatedAmount)
            tvsUser = view.findViewById(R.id.tvsUser)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shelter_all_donations_card, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvsDonateAmount.text = shelterDonations[position].amount.toString() + " CAD"
        viewHolder.tvsUser.text = shelterDonations[position].comment



    }

    override fun getItemCount(): Int {
        return shelterDonations.size
    }

}
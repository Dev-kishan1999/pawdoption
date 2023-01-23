package com.yadav.pawdoption.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.view.DonateListDirections
import com.yadav.pawdoption.view.PendingAdoptionFragmentDirections

// Code Reference: https://developer.android.com/develop/ui/views/layout/recyclerview#kotlin

class DonateAdapter(private val context: Context, private val shelterList: ArrayList<Shelter>) :
    RecyclerView.Adapter<DonateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView
        val tvAddress: TextView
        val tvDescription: TextView
        val bDonate: Button

        init {
            tvName = view.findViewById(R.id.tvShelterName)
            tvAddress = view.findViewById(R.id.tvShelterAddress)
            tvDescription = view.findViewById(R.id.tvShelterDescription)
            bDonate = view.findViewById(R.id.donate_button)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.sheltercard, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvName.text = shelterList[position].name
        viewHolder.tvAddress.text = shelterList[position].address
        viewHolder.tvDescription.text = shelterList[position].description
        viewHolder.bDonate.setOnClickListener{
            val navController = Navigation.findNavController(viewHolder.itemView)

            val action = DonateListDirections.actionDonateListToDonateFragment(
                shelterList[position]
            )

            navController!!.navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return shelterList.size
    }

}


package com.yadav.pawdoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.ShelterPet

// Code Reference: https://developer.android.com/develop/ui/views/layout/recyclerview#kotlin

class ShelterMapAdapter(private val context: Context, private val shelterList: ArrayList<Shelter>) :
    RecyclerView.Adapter<ShelterMapAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView
        val tvAddress: TextView
        val tvDescription: TextView



        init {
            tvName = view.findViewById(R.id.tvShelterName)
            tvAddress = view.findViewById(R.id.tvShelterAddress)
            tvDescription = view.findViewById(R.id.tvShelterDescription)
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

    }

    override fun getItemCount(): Int {
        return shelterList.size
    }

}


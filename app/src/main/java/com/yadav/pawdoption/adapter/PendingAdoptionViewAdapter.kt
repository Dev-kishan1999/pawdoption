//https://developer.android.com/guide/navigation/navigation-pass-data
//https://stackoverflow.com/questions/41156698/loading-images-in-recyclerview-with-picasso-from-api
package com.yadav.pawdoption.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yadav.pawdoption.databinding.PendingAdoptionBinding
import com.yadav.pawdoption.model.PendingAdoptionData
import com.yadav.pawdoption.view.PendingAdoptionFragmentDirections


class PendingAdoptionViewAdapter(private val context: Context, val pendingAdoptionList: MutableList<PendingAdoptionData>): RecyclerView.Adapter<PendingAdoptionViewAdapter.ViewHolder>() {

    private lateinit var binding: PendingAdoptionBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = PendingAdoptionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pendingAdoptionList[position])

        holder.itemView.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)

            val action = PendingAdoptionFragmentDirections.actionPendingAdoptionFragmentToConfirmAdoptionFragment(
                pendingAdoptionList[position].pendionAdoption,
                pendingAdoptionList[position].shelterPet!!,
                pendingAdoptionList[position].user!!,
                pendingAdoptionList[position].shelterPet?.imageURL?.get(0) ?: "https://firebasestorage.googleapis.com/v0/b/pawdoption-fbe00.appspot.com/o/images%2FmyImage?alt=media&token=8ab47cbd-ea28-4517-ae88-220028ad4273"
            )

            navController!!.navigate(action)
        }
    }

    override fun getItemCount() = pendingAdoptionList.size

    inner class ViewHolder(pendingAdoptionView: PendingAdoptionBinding) :
        RecyclerView.ViewHolder(pendingAdoptionView.root) {

        fun bind(pendingAdoption: PendingAdoptionData) {
            binding.apply {
                Picasso.with(context).load(pendingAdoption.shelterPet?.imageURL?.get(0)).into(ivPetImage)
                tvPetName.text = pendingAdoption.shelterPet?.name;
                tvAdopterName.text = pendingAdoption.user?.name;
                tvTimestamp.text = pendingAdoption.pendionAdoption.timestamp!!.split('T')[0];
            }
        }
    }
}
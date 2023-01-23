package com.yadav.pawdoption.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.ShelterPet
import com.yadav.pawdoption.model.UserLovedPet
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.UsersDAO
import com.yadav.pawdoption.view.PetListFragmentDirections
import java.util.*
import kotlin.collections.HashMap


// Code Reference: https://developer.android.com/develop/ui/views/layout/recyclerview#kotlin
// https://stackoverflow.com/a/35306315
// https://github.com/bumptech/glide
class PetListAdapter(private val context: Context, private var petsList: MutableList<ShelterPet>) :
    RecyclerView.Adapter<PetListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petNameTextView: TextView
        val shelterNameTextView: TextView
        val viewDetailsTextView: TextView
        val petBreedTextView: TextView
        val petDescTextView: TextView
        val listItemParent: CardView
        val petImageView: ImageView
        val petLoveImage: ImageView
        val petShareImage: ImageView


        init {
            petNameTextView = view.findViewById(R.id.pet_name)
            shelterNameTextView = view.findViewById(R.id.shelter_name)
            viewDetailsTextView = view.findViewById(R.id.view_details)
            petBreedTextView = view.findViewById(R.id.pet_breed)
            petDescTextView = view.findViewById(R.id.pet_description)
            listItemParent = view.findViewById(R.id.list_item_parent)
            petImageView = view.findViewById(R.id.pet_image)
            petLoveImage = view.findViewById(R.id.love_pet)
            petShareImage = view.findViewById(R.id.share_pet)
        }
    }

    fun setFilteredList(filteredList: MutableList<ShelterPet>) {
        petsList = filteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.pet_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    //    Bind and inflate pet list item with pet details like name, shelter name, image, love and share buttons
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val circularProgressDrawable = CircularProgressDrawable(context)
        val usersDAO = UsersDAO()
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        viewHolder.petNameTextView.text = petsList[position].name
        viewHolder.shelterNameTextView.text = petsList[position].shelterName
        viewHolder.petBreedTextView.text = petsList[position].breed
        viewHolder.petDescTextView.text = petsList[position].description

        Glide.with(context)
            .load(petsList[position].imageURL[0])
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(viewHolder.petImageView);


        viewHolder.itemView.setOnClickListener {
            val navController = Navigation.findNavController(viewHolder.itemView)
            val action = PetListFragmentDirections.actionPetListFragmentToPetDetailFragment(
                petsList[position].shelterId,
                petsList[position].id!!
            )
            navController!!.navigate(action)
        }


        viewHolder.petShareImage.setOnClickListener { view ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I found a pet!")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }

        petsList[position].lovedPetsList.forEach {
            if (petsList[position].shelterId + petsList[position].id == it.value.shelterId + it.value.petId) {
                viewHolder.petLoveImage.setImageResource(R.drawable.ic_round_love_24)
                viewHolder.petLoveImage.tag = it.value.shelterId + it.value.petId
            }
        }
        var currentLovedPets = petsList[position].lovedPetsList

        if (FirebaseDatabaseSingleton.getCurrentUserType().uppercase().equals("PETADOPTER")) {
            viewHolder.petLoveImage.visibility = View.VISIBLE
        } else {
            viewHolder.petLoveImage.visibility = View.GONE
        }

        viewHolder.petLoveImage.setOnClickListener { view ->
            if (viewHolder.petLoveImage.tag.toString() == "false") {
                viewHolder.petLoveImage.setImageResource(R.drawable.ic_round_love_24)
                viewHolder.petLoveImage.tag = petsList[position].shelterId + petsList[position].id
                val userLovedPet = UserLovedPet(
                    UUID.randomUUID().toString(),
                    petsList[position].id,
                    petsList[position].shelterId
                )
                currentLovedPets.put(userLovedPet.id!!, userLovedPet)
                usersDAO.setPetToLoved(FirebaseDatabaseSingleton.getCurrentUid(), currentLovedPets)
            } else {
                viewHolder.petLoveImage.setImageResource(R.drawable.ic_round_love_black_24)
                var filteredList = currentLovedPets.filter {
                    it.value.shelterId + it.value.petId != viewHolder.petLoveImage.tag
                }
                viewHolder.petLoveImage.tag = "false"
                usersDAO.setPetToLoved(
                    FirebaseDatabaseSingleton.getCurrentUid(),
                    filteredList as HashMap<String, UserLovedPet>
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return petsList.size
    }

}


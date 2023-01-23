//https://firebase.google.com/docs/database/android/read-and-write
package com.yadav.pawdoption.persistence

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.yadav.pawdoption.model.ShelterPet

class PetDAO: IPetDAO {
    val TAG = "PetDAO"

    override fun postPet(shelterId: String, pet: ShelterPet) {
        val sheltersReference = FirebaseDatabaseSingleton.getSheltersReference()
        val petRef = sheltersReference.child(shelterId).child("pets")
        petRef.get().addOnSuccessListener {
            val pets = it.getValue<ArrayList<ShelterPet>>()
            val petsSize = pets?.size ?: 0
            pet.id = petsSize.toString()
            petRef.child(petsSize.toString()).setValue(pet)
        }
    }

    override  fun getPet(petId: String, shelterId: String): Task<DataSnapshot> {
        val sheltersReference = FirebaseDatabaseSingleton.getSheltersReference()
        return sheltersReference.child(shelterId).child("pets").child(petId).get()
    }
}
//https://firebase.google.com/docs/database/android/read-and-write
package com.yadav.pawdoption.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.UserLovedPet

class UserLovedPetDAO : IUserLovedPetDAO {

    val TAG: String = "User Loved Pet DAO"

    private var lovedPets = MutableLiveData<HashMap<String, UserLovedPet>>()

    override fun getUserLovedPets(userId: String): MutableLiveData<HashMap<String, UserLovedPet>> {
        FirebaseDatabaseSingleton.getUsersReference().child(userId).child("lovedPets").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lovedPets.value = snapshot.getValue<HashMap<String, UserLovedPet>>()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
        return lovedPets
    }

    override fun addLovedPet(userId: String, lovedPet: UserLovedPet){

        FirebaseDatabaseSingleton.getUsersReference().child(userId).child("lovedPets").child(lovedPet.id!!).setValue(lovedPet );
    }

    override fun deleteLovedPet(userId: String, id: String){


        FirebaseDatabaseSingleton.getUsersReference().child(userId).child("lovedPets").child(id).removeValue()
    }
}
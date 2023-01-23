//https://firebase.google.com/docs/database/android/read-and-write
package com.yadav.pawdoption.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yadav.pawdoption.model.*
import javax.security.auth.login.LoginException

class UsersDAO : IUsersDAO {
    val TAG = "UsersDAO"

    private var users = MutableLiveData<HashMap<String, User>>()
    private var usersType = MutableLiveData<String>()
    private var lovedPets = MutableLiveData<HashMap<String,UserLovedPet>>()

    override fun getUserList(): MutableLiveData<HashMap<String, User>> {
        val usersReference = FirebaseDatabaseSingleton.getUsersReference()
        usersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.value = snapshot.getValue<HashMap<String, User>>()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
        return users
    }

    override fun setCurrentUserTypeByUid(uid: String) {
        val usersReference = FirebaseDatabaseSingleton.getUserTypeReference()
        usersReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.value == null) {
                    usersType.value = "PetAdopter"
                }
                usersType.value = snapshot.value as String

                FirebaseDatabaseSingleton.setCurrentUserType(snapshot.value as String)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    override fun getCurrentUserTypeByUid(): MutableLiveData<String> {
        return usersType
    }

    override fun  getUserById(userId: String): Task<DataSnapshot> {

        return FirebaseDatabaseSingleton.getUsersReference().child(userId).get()
    }

    override fun setPetToLoved(userId: String, lovedPets: HashMap<String,UserLovedPet>) {
        val usersReference = FirebaseDatabaseSingleton.getUsersReference()
        usersReference.child(userId).child("lovedPets").setValue(lovedPets)
    }

    override fun getLovedPetsByUid(userId: String): MutableLiveData<HashMap<String,UserLovedPet>> {
        val usersReference = FirebaseDatabaseSingleton.getUsersReference()
        usersReference.child(userId).child("lovedPets")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null)
                        lovedPets.value = hashMapOf()
                    else {
                        lovedPets.value = snapshot.getValue<HashMap<String,UserLovedPet>>()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }

            })
        return lovedPets
    }

}
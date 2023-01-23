package com.yadav.pawdoption.persistence

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.yadav.pawdoption.model.User
import com.yadav.pawdoption.model.UserLovedPet

interface IUsersDAO {

    fun getUserList(): MutableLiveData<HashMap<String, User>>
    fun setCurrentUserTypeByUid(uid: String)
    fun getCurrentUserTypeByUid(): MutableLiveData<String>
    fun getUserById(userId: String): Task<DataSnapshot>
    fun setPetToLoved(userId: String, lovedPets: HashMap<String,UserLovedPet>)
    fun getLovedPetsByUid(userId: String) : MutableLiveData<HashMap<String,UserLovedPet>>
}
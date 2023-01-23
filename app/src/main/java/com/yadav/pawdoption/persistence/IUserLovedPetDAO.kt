package com.yadav.pawdoption.persistence

import androidx.lifecycle.MutableLiveData
import com.yadav.pawdoption.model.UserLovedPet

interface IUserLovedPetDAO {
    fun getUserLovedPets(userId: String): MutableLiveData<HashMap<String, UserLovedPet>>

    fun addLovedPet(userId: String, lovedPet: UserLovedPet)
    fun deleteLovedPet(userId: String, id: String)
}
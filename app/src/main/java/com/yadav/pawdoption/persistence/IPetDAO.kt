package com.yadav.pawdoption.persistence

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.yadav.pawdoption.model.ShelterPet

interface IPetDAO {
    fun postPet(shelterId: String, pet: ShelterPet)
    fun getPet(petId: String, shelterId: String): Task<DataSnapshot>
}
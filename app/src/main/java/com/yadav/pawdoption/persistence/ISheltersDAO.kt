package com.yadav.pawdoption.persistence

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.yadav.pawdoption.model.Shelter
import com.yadav.pawdoption.model.ShelterPet

interface ISheltersDAO {
    fun getPetsList()
    fun getPetsListByShelterId(shelterId: String): MutableList<ShelterPet>
    fun getShelters(): MutableLiveData<HashMap<String, Shelter>>
    fun getShelterById(shelterId: String): Task<DataSnapshot>
}
package com.yadav.pawdoption.persistence

import androidx.lifecycle.MutableLiveData
import com.yadav.pawdoption.model.PendingAdoption


interface IPendingAdoptionDAO {
    fun getAdoptionList(shelterId: String): MutableLiveData<HashMap<String, PendingAdoption>>

    fun deletePendingAdoption(id: String, shelterId: String)
    fun addPendingAdoption(shelterId: String, pendingAdoption: PendingAdoption)
}
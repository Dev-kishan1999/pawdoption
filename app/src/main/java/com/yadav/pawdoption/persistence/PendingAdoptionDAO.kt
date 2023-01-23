//https://firebase.google.com/docs/database/android/read-and-write
package com.yadav.pawdoption.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yadav.pawdoption.model.PendingAdoption
import com.yadav.pawdoption.model.Shelter

class PendingAdoptionDAO : IPendingAdoptionDAO {

    val TAG = "PendingAdoptionDAO"

    val pendingAdoptions = MutableLiveData<HashMap<String, PendingAdoption>>();


    override fun getAdoptionList(shelterId: String): MutableLiveData<HashMap<String, PendingAdoption>> {
        val shelterReference = FirebaseDatabaseSingleton.getSheltersReference()
        shelterReference.child(shelterId).child("pendingAdoptions").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingAdoptions.value = snapshot.getValue<HashMap<String, PendingAdoption>>()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

        return pendingAdoptions
    }

    override fun deletePendingAdoption(id: String, shelterId: String){

        FirebaseDatabaseSingleton.getSheltersReference().child(shelterId)
            .child("pendingAdoptions")
            .child(id)
            .removeValue()
    }

    override fun addPendingAdoption(shelterId: String, pendingAdoption: PendingAdoption){

        val shelterRef = FirebaseDatabaseSingleton.getSheltersReference().child(shelterId)

        shelterRef.child("pendingAdoptions").child(pendingAdoption.id!!).setValue(pendingAdoption)
    }

}
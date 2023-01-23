package com.yadav.pawdoption.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
//@IgnoreExtraProperties
data class PendingAdoption(

    @SerializedName("id"                    ) var id                  : String? = null,
    @SerializedName("user_id"                    ) var userId                  : String?  = null,
    @SerializedName("pet_id"                     ) var petId                   : String?  = null,
    @SerializedName("timestamp"                  ) var timestamp               : String?  = null,
//    @SerializedName("can_provide_adequate_water" ) var canProvideAdequateWater : Boolean? = null,
//    @SerializedName("can_provide_adequate_food"  ) var canProvideAdequateFood  : Boolean? = null,
//    @SerializedName("have_enough_space"          ) var haveEnoughSpace         : Boolean? = null,
//    @SerializedName("have_dog_house"             ) var haveDogHouse            : Boolean? = null,
//    @SerializedName("any_allergic"               ) var anyAllergic             : Boolean? = null


) : Parcelable

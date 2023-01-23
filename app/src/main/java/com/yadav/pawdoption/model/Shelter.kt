//https://stackoverflow.com/questions/51380056/android-expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
//https://stackoverflow.com/questions/61758963/how-to-ignore-fields-when-using-parcelize-annotation-in-kotlin
package com.yadav.pawdoption.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shelter (

    @SerializedName("id") var id          : String? = null,
    @SerializedName("name") var name        : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("address") var address     : String? = null,
    @SerializedName("latitude"    ) var latitude    : Double? = null,
    @SerializedName("longitude"   ) var longitude   : Double? = null,

):Parcelable{
    @IgnoredOnParcel
    @SerializedName("pets"        ) var pets        : ArrayList<ShelterPet> = arrayListOf()

    @IgnoredOnParcel
    @SerializedName("donations"   ) var donations   : HashMap<String,ShelterDonation> = hashMapOf()

    @IgnoredOnParcel
    @SerializedName("veterinarians") var veterinarians   : ArrayList<Veterinarian> = arrayListOf()

    @IgnoredOnParcel
    @SerializedName("pending_adoptions") var pendingAdoptions   : HashMap<String, PendingAdoption> = hashMapOf()
}
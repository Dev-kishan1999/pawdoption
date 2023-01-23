package com.yadav.pawdoption.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

//https://stackoverflow.com/questions/61758963/how-to-ignore-fields-when-using-parcelize-annotation-in-kotlin
//https://stackoverflow.com/questions/51380056/android-expected-a-list-while-deserializing-but-got-a-class-java-util-hashmap
@Parcelize
data class User(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("mobile_number")
    var mobileNumber: String? = null,

    @SerializedName("image")
    var image: String? = null,
) : Parcelable {
    @IgnoredOnParcel
    @SerializedName("pets")
    var pets: ArrayList<UserPet>? = null

    @IgnoredOnParcel
    @SerializedName("appointments")
    var appointments: ArrayList<Appointment>? = null

    @IgnoredOnParcel
    @SerializedName("lovedPets")
    var lovedPets: HashMap<String,UserLovedPet>? = null

    @IgnoredOnParcel
    @SerializedName("donations")
    var donations: HashMap<String,UserDonation>? = hashMapOf()
}
package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName

data class UserLovedPet(

    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("pet_id"     ) var petId     : String? = null,
    @SerializedName("shelter_id" ) var shelterId : String? = null

)
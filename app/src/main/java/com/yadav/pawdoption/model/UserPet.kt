package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName

data class UserPet(

    @SerializedName("pet_id"     ) var petId     : String? = null,
    @SerializedName("shelter_id" ) var shelterId : String? = null

)

package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName


data class ShelterData (

    @SerializedName("shelter_id")
    var shelterId : String? = null,

    @SerializedName("shelter_name")
    var shelterName : String? = null,

    @SerializedName("shelter_address")
    var shelterAddress : String? = null,

    @SerializedName("shelter_description")
    var shelterDescription : String? = null,

    @SerializedName("lat")
    var lat : String? = null,

    @SerializedName("long")
    var long : String? = null,

//    @SerializedName("pets")
//    var pets : ArrayList<Pets> = null

)

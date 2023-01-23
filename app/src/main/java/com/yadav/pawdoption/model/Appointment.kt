package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName


data class Appointment (

    @SerializedName("shelterName")
    val shelterName: String? = null,
    @SerializedName("vetName")
    val vetName: String? = null,
    @SerializedName("vetQualification")
    val vetQualification: String? = null,
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("time")
    val time: String? = null

)
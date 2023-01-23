package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName


data class Veterinarian (

    @SerializedName("id"            ) var id            : String?           = null,
    @SerializedName("name"          ) var name          : String?           = null,
    @SerializedName("qualification" ) var qualification : String?           = null,
    @SerializedName("days"          ) var days          : ArrayList<String> = arrayListOf(),
    @SerializedName("bookedSlots"  ) var bookedSlots   : ArrayList<BookedSlot> = arrayListOf()
)
package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName


data class BookedSlot (

    @SerializedName("id"       ) var id       : String? = null,
    @SerializedName("day"      ) var day      : String? = null,
    @SerializedName("timeslot" ) var timeSlot : String? = null,
    @SerializedName("user_id"  ) var userId   : String? = null

)
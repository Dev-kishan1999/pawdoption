package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName


data class ShelterDonation (


    @SerializedName("id"        ) var id        : String? = null,
    @SerializedName("user_id"    ) var userID    : String? = null,
//    @SerializedName("shelter_id" ) var shelterID : String? = null,
    @SerializedName("comment"   ) var comment   : String? = null,
    @SerializedName("amount"    ) var amount    : Double? = null,
    @SerializedName("timestamp" ) var timestamp : String? = null

)
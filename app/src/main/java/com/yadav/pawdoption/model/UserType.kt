package com.yadav.pawdoption.model

import com.google.gson.annotations.SerializedName


data class UserType(

    @SerializedName("userType")
    var userType: HashMap<String,String>? = null,

)
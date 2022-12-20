package com.example.languagetestapp.feature_auth.data.model

import com.google.gson.annotations.SerializedName

class UserDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("_id")
    val _id: String? = null
)
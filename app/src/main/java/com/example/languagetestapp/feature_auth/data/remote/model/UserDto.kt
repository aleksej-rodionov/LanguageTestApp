package com.example.languagetestapp.feature_auth.data.remote.model

import com.example.languagetestapp.feature_auth.domain.model.User
import com.google.gson.annotations.SerializedName

class UserDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("_id")
    val _id: String? = null
) {

    fun toUser(): User {
        return User(email, password, _id)
    }
}
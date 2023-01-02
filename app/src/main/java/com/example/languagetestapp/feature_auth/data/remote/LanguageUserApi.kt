package com.example.languagetestapp.feature_auth.data.remote

import com.example.languagetestapp.feature_auth.data.remote.model.AuthResponse
import com.example.languagetestapp.feature_auth.data.remote.model.UserDto
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface LanguageUserApi {

    @GET("/users/current")
    suspend fun getCurrentUserInfo(): UserResponse<UserDto>

    @FormUrlEncoded
    @PUT("/users/change-password")
    suspend fun changePassword(
        @Field("oldpassword") oldPassword: String,
        @Field("newpassword") newPassword: String
    ): AuthResponse<String>
}



data class UserResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("body")
    val body: T?,
    @SerializedName("error")
    val error: String?
)
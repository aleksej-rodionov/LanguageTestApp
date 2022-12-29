package com.example.languagetestapp.feature_auth.data.remote

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_notes.data.remote.model.NoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface LanguageUserApi {

    @GET("/users/current")
    suspend fun getCurrentUserInfo(): UserResponse<List<NoteDto>>
}



data class UserResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("body")
    val body: T?,
    @SerializedName("error")
    val error: String?
)
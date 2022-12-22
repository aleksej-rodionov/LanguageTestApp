package com.example.languagetestapp.feature_notes.data.remote.model

import com.google.gson.annotations.SerializedName

data class NoteResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("body")
    val body: T?,
    @SerializedName("error")
    val error: String?
)

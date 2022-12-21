package com.example.languagetestapp.feature_notes.data.model

import com.google.gson.annotations.SerializedName

data class NoteDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("text")
    val text: String
)

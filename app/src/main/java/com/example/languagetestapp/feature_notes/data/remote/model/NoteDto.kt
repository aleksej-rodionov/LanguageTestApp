package com.example.languagetestapp.feature_notes.data.remote.model

import com.example.languagetestapp.feature_notes.domain.model.Note
import com.google.gson.annotations.SerializedName

data class NoteDto(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("text")
    val text: String
) {

    fun toNote(): Note {
        return Note(_id, email, text)
    }
}

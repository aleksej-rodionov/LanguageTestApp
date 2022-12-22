package com.example.languagetestapp.feature_notes.data.model

import com.example.languagetestapp.feature_notes.domain.model.Note
import com.google.gson.annotations.SerializedName

data class NoteDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("text")
    val text: String
) {

    fun toNote(): Note {
        return Note(email, text)
    }
}

package com.example.languagetestapp.feature_notes.data.remote.model

import com.example.languagetestapp.feature_notes.domain.model.NewNote
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.google.gson.annotations.SerializedName

data class NewNoteDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("text")
    val text: String
) {

    companion object {
        fun fromNote(note: NewNote): NewNoteDto {
            return NewNoteDto(note.email, note.text)
        }
    }
}

package com.example.languagetestapp.feature_notes.domain.repo

import com.example.languagetestapp.feature_notes.data.model.NoteDto
import com.example.languagetestapp.feature_notes.data.model.NoteResponse

interface NoteRepo {

    suspend fun createNote(note: NoteDto): NoteResponse<NoteDto>

    suspend fun getNotesByEmail(email: String): NoteResponse<List<NoteDto>>

    suspend fun getAllNotes(): NoteResponse<List<NoteDto>>
}
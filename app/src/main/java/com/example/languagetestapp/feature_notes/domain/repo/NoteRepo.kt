package com.example.languagetestapp.feature_notes.domain.repo

import com.example.languagetestapp.feature_notes.data.model.NoteDto
import com.example.languagetestapp.feature_notes.data.model.NoteResponse

interface NoteRepo {

    suspend fun createNote(note: NoteDto): NoteResponse<NoteDto>

    suspend fun getNotes(): NoteResponse<List<NoteDto>>

    suspend fun getNoteById(noteId: String): NoteResponse<NoteDto>
}
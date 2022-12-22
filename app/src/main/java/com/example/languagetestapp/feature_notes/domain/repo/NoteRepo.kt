package com.example.languagetestapp.feature_notes.domain.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.data.remote.model.NoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteResponse
import com.example.languagetestapp.feature_notes.domain.model.Note

interface NoteRepo {

    suspend fun createNote(note: NoteDto): NoteResponse<NoteDto>

    suspend fun getNotes(): Resource<List<Note>>

    suspend fun getNoteById(noteId: String): NoteResponse<NoteDto>
}
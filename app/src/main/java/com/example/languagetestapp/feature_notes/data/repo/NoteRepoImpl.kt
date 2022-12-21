package com.example.languagetestapp.feature_notes.data.repo

import com.example.languagetestapp.feature_notes.data.model.NoteDto
import com.example.languagetestapp.feature_notes.data.model.NoteResponse
import com.example.languagetestapp.feature_notes.data.remote.LanguageNoteApi
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo

class NoteRepoImpl(
    private val noteApi: LanguageNoteApi
): NoteRepo {

    override suspend fun createNote(note: NoteDto): NoteResponse<NoteDto> {
        return noteApi.createNote(note)
    }

    override suspend fun getNotesByEmail(email: String): NoteResponse<List<NoteDto>> {
        return noteApi.getNotesByEmail(email)
    }

    override suspend fun getAllNotes(): NoteResponse<List<NoteDto>> {
        return noteApi.getAllNotes()
    }
}
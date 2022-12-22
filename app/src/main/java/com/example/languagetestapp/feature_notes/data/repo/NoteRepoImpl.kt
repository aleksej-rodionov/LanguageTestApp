package com.example.languagetestapp.feature_notes.data.repo

import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.data.remote.model.NoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteResponse
import com.example.languagetestapp.feature_notes.data.remote.LanguageNoteApi
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo

class NoteRepoImpl(
    private val noteApi: LanguageNoteApi
): NoteRepo {

    override suspend fun createNote(note: NoteDto): NoteResponse<NoteDto> {
        return noteApi.createNote(note)
    }

    override suspend fun getNotes(): Resource<List<Note>> {
        val response = noteApi.getNotes()

        if (response.status == "ok") {

            response.body?.let {

                // todo cache data in RoomDb

                val notes = it.map { dto ->
                    dto.toNote()
                }

                return Resource.Success(notes)
            } ?: run {
                return Resource.Error("Response is successful, but notes not found")
            }

        } else {
            return Resource.Error(response.error ?: "Unknown error occurred")
        }
    }

    override suspend fun getNoteById(noteId: String): NoteResponse<NoteDto> {
        return noteApi.getNoteById(noteId)
    }
}
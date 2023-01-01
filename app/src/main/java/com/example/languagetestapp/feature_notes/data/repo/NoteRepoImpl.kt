package com.example.languagetestapp.feature_notes.data.repo

import android.util.Log
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.data.local.AuthStorageGateway
import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_notes.data.remote.model.NoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteResponse
import com.example.languagetestapp.feature_notes.data.remote.LanguageNoteApi
import com.example.languagetestapp.feature_notes.data.remote.model.NewNoteDto
import com.example.languagetestapp.feature_notes.domain.model.NewNote
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import com.example.languagetestapp.feature_notes.util.Constants.TAG_NOTE

class NoteRepoImpl(
    private val noteApi: LanguageNoteApi,
    private val authStorageGateway: AuthStorageGateway
) : NoteRepo {

    // todo rewrite methods below according to DRY

    override suspend fun getNotes(): Resource<List<Note>> {
        try {
            val response = noteApi.getNotes()
            if (response.status == "ok") {
                response.body?.let {
                    // todo cache data in RoomDb
                    val notes = it.map { dto ->
                        dto.toNote()
                    }
                    return Resource.Success(notes)
                } ?: run {
                    return Resource.Error("Success but body is null")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun getNoteById(noteId: String): Resource<Note> {
        try {
            val response = noteApi.getNoteById(noteId)
            if (response.status == "ok") {
                response.body?.let { dto ->
                    val note = dto.toNote()
                    return Resource.Success(note)
                } ?: run {
                    return Resource.Error("Success but body is null")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun createNote(text: String): Resource<Note> {
        val curUserData = fetchCurrentUserData()
        Log.d(TAG_NOTE, "curUserFromShared = $curUserData")
        val email = curUserData?.email
//        val email = fetchCurrentUserData()?.email
//        Log.d(TAG_NOTE, "email from storage = ${email ?: "NULL"}")
        if (email.isNullOrBlank()) return Resource.Error("You are not logged in")
        val newNote = NewNote(
            email = email,
            text = text
        )

        try {
            val response = noteApi.createNote(NewNoteDto.fromNote(newNote))
            if (response.status == "ok") {
                response.body?.let { dto ->
                    val noteCreated = dto.toNote()
                    return Resource.Success(noteCreated)
                } ?: run {
                    return Resource.Error("Success but body is null")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun updateNote(noteId: String, newNote: Note): Resource<Note> {
        try {
            val response = noteApi.updateNote(noteId, NoteDto.fromNote(newNote))
            if (response.status == "ok") {
                response.body?.let { dto ->
                    val noteUpdated = dto.toNote()
                    return Resource.Success(noteUpdated)
                } ?: run {
                    return Resource.Error("Success but body is null")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override suspend fun deleteNote(noteId: String): Resource<Note> {
        try {
            val response = noteApi.deleteNote(noteId)
            if (response.status == "ok") {
                response.body?.let { dto ->
                    val noteDeleted = dto.toNote()
                    return Resource.Success(noteDeleted)
                } ?: run {
                    return Resource.Error("Success but body is null")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override fun fetchCurrentUserData(): User? {
        return authStorageGateway.fetchCurrentUserData()
    }
}










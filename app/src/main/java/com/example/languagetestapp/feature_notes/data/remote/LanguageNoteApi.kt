package com.example.languagetestapp.feature_notes.data.remote

import com.example.languagetestapp.feature_notes.data.remote.model.NewNoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteResponse
import retrofit2.http.*

interface LanguageNoteApi {

    @GET("/notes")
    suspend fun getNotes(): NoteResponse<List<NoteDto>>

    @POST("/notes")
    suspend fun createNote(@Body note: NewNoteDto): NoteResponse<NoteDto>

    @GET("/notes/{noteid}")
    suspend fun getNoteById(@Path("noteid") noteId: String): NoteResponse<NoteDto>

    @PUT("/notes/{noteid}")
    suspend fun updateNote(@Path("noteid") noteId: String, @Body note: NoteDto): NoteResponse<NoteDto>

    @DELETE("/notes/{noteid}")
    suspend fun deleteNote(@Path("noteid") noteId: String): NoteResponse<String>
}
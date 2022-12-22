package com.example.languagetestapp.feature_notes.data.remote

import com.example.languagetestapp.feature_notes.data.remote.model.NoteDto
import com.example.languagetestapp.feature_notes.data.remote.model.NoteResponse
import retrofit2.http.*

interface LanguageNoteApi {

    @POST("/notes")
    suspend fun createNote(@Body note: NoteDto): NoteResponse<NoteDto>

    @GET("/notes")
    suspend fun getNotes(): NoteResponse<List<NoteDto>>

    @FormUrlEncoded
    @GET("/notes")
    suspend fun getNoteById(@Field("noteid") noteId: String): NoteResponse<NoteDto>
}
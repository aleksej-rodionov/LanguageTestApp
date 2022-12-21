package com.example.languagetestapp.feature_notes.data.remote

import com.example.languagetestapp.feature_notes.data.model.NoteDto
import com.example.languagetestapp.feature_notes.data.model.NoteResponse
import retrofit2.http.*

interface LanguageNoteApi {

    @POST("/notes")
    suspend fun createNote(@Body note: NoteDto): NoteResponse<NoteDto>

    @FormUrlEncoded
    @GET("/notes")
    suspend fun getNotesByEmail(@Field("email") email: String): NoteResponse<List<NoteDto>>

    @GET("/notes")
    suspend fun getAllNotes(): NoteResponse<List<NoteDto>>
}
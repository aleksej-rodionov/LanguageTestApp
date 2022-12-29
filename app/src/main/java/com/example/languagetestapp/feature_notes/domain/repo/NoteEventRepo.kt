package com.example.languagetestapp.feature_notes.domain.repo

import com.example.languagetestapp.feature_notes.domain.model.Note
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

// todo can I make it a reusable interface?
interface NoteEventRepo {

    val noteCreated: SharedFlow<Note>

    fun onNoteCreated(note: Note?)


    val noteUpdated: SharedFlow<Note>

    fun onNoteUpdated(note: Note?)


    val noteDeleted: SharedFlow<Note>

    fun onNoteDeleted(note: Note?)
}
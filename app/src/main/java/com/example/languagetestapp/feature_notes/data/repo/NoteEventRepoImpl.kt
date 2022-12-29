package com.example.languagetestapp.feature_notes.data.repo

import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteEventRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NoteEventRepoImpl (
    private val appScope: CoroutineScope
): NoteEventRepo {

    private val _noteCreated = MutableSharedFlow<Note>()
    override val noteCreated: SharedFlow<Note> = _noteCreated.asSharedFlow()

    override fun onNoteCreated(note: Note?) {
        note?.let {
            appScope.launch {
                _noteCreated.emit(it)
            }
        }
    }


    private val _noteUpdated = MutableSharedFlow<Note>()
    override val noteUpdated: SharedFlow<Note> = _noteUpdated.asSharedFlow()

    override fun onNoteUpdated(note: Note?) {
        note?.let {
            appScope.launch {
                _noteUpdated.emit(it)
            }
        }
    }


    private val _noteDeleted = MutableSharedFlow<Note>()
    override val noteDeleted: SharedFlow<Note> = _noteDeleted.asSharedFlow()

    override fun onNoteDeleted(note: Note?) {
        note?.let {
            appScope.launch {
                _noteDeleted.emit(it)
            }
        }
    }
}
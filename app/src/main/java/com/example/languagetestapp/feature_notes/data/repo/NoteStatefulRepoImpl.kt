package com.example.languagetestapp.feature_notes.data.repo

import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteStatefulRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NoteStatefulRepoImpl (
    private val noteScope: CoroutineScope
): NoteStatefulRepo {

    private val _noteCreated = MutableSharedFlow<Note>()
    override val noteCreated: SharedFlow<Note> = _noteCreated.asSharedFlow()

    override fun onNoteCreated(note: Note?) {
        note?.let {
            noteScope.launch {
                _noteCreated.emit(it)
            }
        }
    }


    private val _noteUpdated = MutableSharedFlow<Note>()
    override val noteUpdated: SharedFlow<Note> = _noteUpdated.asSharedFlow()

    override fun onNoteUpdated(note: Note?) {
        note?.let {
            noteScope.launch {
                _noteUpdated.emit(it)
            }
        }
    }


    private val _noteDeleted = MutableSharedFlow<Note>()
    override val noteDeleted: SharedFlow<Note> = _noteDeleted.asSharedFlow()

    override fun onNoteDeleted(note: Note?) {
        note?.let {
            noteScope.launch {
                _noteDeleted.emit(it)
            }
        }
    }
}
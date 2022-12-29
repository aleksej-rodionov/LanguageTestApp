package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import com.example.languagetestapp.feature_notes.domain.repo.NoteEventRepo
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val noteEventRepo: NoteEventRepo,
    private val authRepo: AuthRepo
): ViewModel() {

    var state by mutableStateOf(NoteListState())

    private val _uiEvent = Channel<NoteListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: NoteListAction) {
        when (action) {
            is NoteListAction.OnCreateNoteClick -> {
                viewModelScope.launch {
                    _uiEvent.send(NoteListUiEvent.Navigate(NoteDest.NoteDetailsDest.route))
                }
            }
            is NoteListAction.OnNoteClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        NoteListUiEvent.Navigate(
                            NoteDest.NoteDetailsDest.route + "?noteId=${action.note._id}"))
                }
            }
            is NoteListAction.OnDeleteNoteClick -> {
                // todo
            }
            is NoteListAction.OnCompletedChanged -> {
                // todo
            }
        }
    }

    init {
        fetchNotes()
        subscribeToNoteEvents()
    }

    private fun subscribeToNoteEvents() {
        viewModelScope.launch {
            noteEventRepo.noteCreated.collect {
                state = state.copy(notes = listWithNewNote(it))
            }
        }

        viewModelScope.launch {
            noteEventRepo.noteUpdated.collect {
                state = state.copy(notes = listWithNoteChanges(it))
            }
        }

        viewModelScope.launch {
            noteEventRepo.noteDeleted.collect {
                state = state.copy(notes = listWithoutDeletedNote(it))
            }
        }
    }

    private fun listWithNewNote(note: Note): List<Note> {
        val list = mutableListOf<Note>()
        list.addAll(state.notes)
        list.add(note)
        return list
    }

    private fun listWithNoteChanges(note: Note): List<Note> {
        val list = state.notes.map {
            if (it._id == note._id) note else it
        }
        return list
    }

    private fun listWithoutDeletedNote(note: Note): List<Note> {
        val list = state.notes.filter {
            it._id != note._id
        }
        return list
    }

    private fun fetchNotes() = viewModelScope.launch {
        state = state.copy(isLoading = true)

        try {
            when (val result = noteRepo.getNotes()) {
                is Resource.Success -> {
                    state = state.copy(
                        notes = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        notes = result.data ?: emptyList(),
                        isLoading = false
                    )
                    _uiEvent.send(NoteListUiEvent.SnackbarMsg(result.message ?: "Unknown error"))
                }
                is Resource.Loading -> { // todo never happens since is never sent from NoteRepo
                    state = state.copy(
                        notes = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
            }
        } catch (e: Exception) {
            state = state.copy(isLoading = false)
            viewModelScope.launch {
                _uiEvent.send(
                    NoteListUiEvent.SnackbarMsg(
                        e.message ?: "Unknown error on ViewModel layer"
                    )
                )
            }
        }
    }
}

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false
)

sealed class NoteListAction {
    object OnCreateNoteClick: NoteListAction()
    data class OnNoteClick(val note: Note): NoteListAction()
    data class OnDeleteNoteClick(val note: Note): NoteListAction()
    data class OnCompletedChanged(val note: Note, val checked: Boolean): NoteListAction()
}

sealed class NoteListUiEvent {
    data class Navigate(val route: String): NoteListUiEvent()
    data class SnackbarMsg(val msg: String): NoteListUiEvent()
}
package com.example.languagetestapp.feature_notes.presentation.note_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteStatefulRepo
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest
import com.example.languagetestapp.feature_notes.util.Constants.TAG_NOTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val noteStatefulRepo: NoteStatefulRepo
) : ViewModel() {

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
                            NoteDest.NoteDetailsDest.route + "?noteId=${action.note._id}"
                        )
                    )
                }
            }
            is NoteListAction.OnDeleteNoteClick -> {
                deleteNote(action.note)
            }
            is NoteListAction.OnCompletedChanged -> {
                // todo
            }
            is NoteListAction.SearchWidgetStateChanged -> {
                state = state.copy(searchWidgetState = action.state)
            }
            is NoteListAction.SearchTextChanged -> {
                onSearch(action.text)
            }
        }
    }

    init {
        fetchNotes()
//        fetchNotesByQuery("")
        subscribeToNoteEvents()
    }

    fun onPullRefresh() {
        state = state.copy(isRefreshing = true)
        fetchNotes()
    }

    private var searchJob: Job? = null
    private fun onSearch(query: String) {
        state = state.copy(searchText = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            fetchNotesByQuery(query)
        }
    }

    private suspend fun fetchNotesByQuery(query: String) {
        state = state.copy(isLoading = true)
        val result = noteRepo.searchNotes(query)
        when (result) {
            is Resource.Success -> {
                state = state.copy(
                    notes = result.data ?: emptyList(),
                    isLoading = false
                )
            }
            is Resource.Error -> {
                state = state.copy(isLoading = false)
                _uiEvent.send(NoteListUiEvent.SnackbarMsg(result.message ?: "Unknown error"))
            }
        }
    }

    private fun fetchNotes() = viewModelScope.launch {
        state = state.copy(isLoading = true)
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
        }
        state = state.copy(isRefreshing = false)
    }

    private fun subscribeToNoteEvents() {
        viewModelScope.launch {
            noteStatefulRepo.noteCreated.collect {
                Log.d(TAG_NOTE, "noteCreated: $it")
                state = state.copy(notes = listWithNewNote(it))
            }
        }

        viewModelScope.launch {
            noteStatefulRepo.noteUpdated.collect {
                Log.d(TAG_NOTE, "noteUpdated: $it")
                state = state.copy(notes = listWithNoteChanges(it))
            }
        }

        viewModelScope.launch {
            noteStatefulRepo.noteDeleted.collect {
                Log.d(TAG_NOTE, "noteDeleted: $it")
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

    private fun deleteNote(note: Note) = viewModelScope.launch {
        when (val resp = noteRepo.deleteNote(note._id)) {
            is Resource.Success -> {
                noteStatefulRepo.onNoteDeleted(resp.data)

                // todo add UNDO btn to the snackbar below
                _uiEvent.send(
                    NoteListUiEvent.SnackbarMsg(
                        "Note successfully deleted"
                    )
                )
            }
            is Resource.Error -> {
                _uiEvent.send(
                    NoteListUiEvent.SnackbarMsg(
                        resp.message ?: "Unknown error on ViewModel layer"
                    )
                )
            }
        }
    }
}

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchWidgetState: SearchWidgetState = SearchWidgetState.Closed,
    val searchText: String = "",
)

sealed class NoteListAction {
    object OnCreateNoteClick : NoteListAction()
    data class OnNoteClick(val note: Note) : NoteListAction()
    data class OnDeleteNoteClick(val note: Note) : NoteListAction()
    data class OnCompletedChanged(val note: Note, val checked: Boolean) : NoteListAction()
    data class SearchWidgetStateChanged(val state: SearchWidgetState): NoteListAction()
    data class SearchTextChanged(val text: String): NoteListAction()
}

sealed class NoteListUiEvent {
    data class Navigate(val route: String) : NoteListUiEvent()
    data class SnackbarMsg(val msg: String) : NoteListUiEvent()
}
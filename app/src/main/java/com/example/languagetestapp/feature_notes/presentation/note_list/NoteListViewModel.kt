package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepo: NoteRepo
): ViewModel() {

    private val _state = mutableStateOf(NoteListState())
    val state: State<NoteListState> = _state

    private val _uiEvent = Channel<NoteListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchNotes()
    }

    private fun fetchNotes() = viewModelScope.launch {
        _state.value = state.value.copy(isLoading = true)
        val result = noteRepo.getNotes()
        when (result) {
            is Resource.Success -> {
                _state.value = state.value.copy(
                    notes = result.data ?: emptyList(),
                    isLoading = false
                )
            }
            is Resource.Error -> {
                _state.value = state.value.copy(
                    notes = result.data ?: emptyList(),
                    isLoading = false
                )
                _uiEvent.send(NoteListUiEvent.SnackbarMsg(result.message ?: "Unknown error"))
            }
            is Resource.Loading -> { // never happens since is never sent from NoteRepo
                _state.value = state.value.copy(
                    notes = result.data ?: emptyList(),
                    isLoading = true
                )
            }
        }
    }

    fun onAction(action: NoteListAction) {
        when (action) {
            is NoteListAction.OnCreateNoteClick -> {
                // todo create note logic
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
}

sealed class NoteListUiEvent {
    data class Navigate(val route: String): NoteListUiEvent()
    data class SnackbarMsg(val msg: String): NoteListUiEvent()
}
package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.presentation.login.LoginState
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

    var state by mutableStateOf(NoteListState())

    private val _uiEvent = Channel<NoteListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchNotes()
    }

    private fun fetchNotes() = viewModelScope.launch {
        state = state.copy(isLoading = true)
        val result = noteRepo.getNotes()
        when (result) {
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
            is Resource.Loading -> { // never happens since is never sent from NoteRepo
                state = state.copy(
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
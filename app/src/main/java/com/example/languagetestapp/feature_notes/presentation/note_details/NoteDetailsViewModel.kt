package com.example.languagetestapp.feature_notes.presentation.note_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var noteId: String? = null
    var noteEdited: Note? = null

    var state by mutableStateOf(NoteDetailsState())

    private val _uiEvent = Channel<NoteDetailsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: NoteDetailsAction) {
        when (action) {
            is NoteDetailsAction.OnTextChanged -> {
                state = state.copy(text = action.text)
            }
            is NoteDetailsAction.OnClickSave -> {
                viewModelScope.launch {
                    if (state.text.isBlank()) {
                        _uiEvent.send(NoteDetailsUiEvent.SnackbarMsg("Text can't be empty"))
                        return@launch
                    }

                    if (noteId == null) {
                        try {
                            when (val resp = noteRepo.createNote(state.text)) {
                                is Resource.Success -> {
                                    state = state.copy(isLoading = false)
                                }
                                is Resource.Error -> {
                                    state = state.copy(isLoading = false)
                                }
                                is Resource.Loading -> { // todo remove this subclass
                                    state = state.copy(isLoading = true)
                                }
                            }
                        } catch (e: Exception) {
                            state = state.copy(isLoading = false)
                            viewModelScope.launch {
                                _uiEvent.send(NoteDetailsUiEvent.SnackbarMsg(
                                    e.message ?: "Unknown error on ViewModel layer"
                                ))
                            }
                        }

                    } else {
                        try {
                            when (val resp = noteRepo.updateNote(noteId!!, noteEdited!!)) {
                                is Resource.Success -> {
                                    state = state.copy(isLoading = false)
                                }
                                is Resource.Error -> {
                                    state = state.copy(isLoading = false)
                                }
                                is Resource.Loading -> { // todo remove this subclass
                                    state = state.copy(isLoading = true)
                                }
                            }
                        } catch (e: Exception) {
                            state = state.copy(isLoading = false)
                            viewModelScope.launch {
                                _uiEvent.send(NoteDetailsUiEvent.SnackbarMsg(
                                    e.message ?: "Unknown error on ViewModel layer"
                                ))
                            }
                        }

                    }
                    _uiEvent.send(NoteDetailsUiEvent.PopBackStack) // todo send with SnackbarMsg
                }
            }
        }
    }

    init {
        noteId = savedStateHandle.get<String>("noteId")
        noteId?.let { id ->
            getNoteById(id)
        }
    }

    private fun getNoteById(id: String) = viewModelScope.launch {
        state = state.copy(isLoading = true)

        try {
            when (val result = noteRepo.getNoteById(id)) {
                is Resource.Success -> {
                    state = state.copy(
                        text = result.data?.text ?: "",
                        isLoading = false
                    )
                    noteEdited = result.data
                }
                is Resource.Error -> {
                    state = state.copy(
                        text = "",
                        isLoading = false
                    )
                }
                is Resource.Loading -> { // todo never happens, remove Loading subclass
                    state = state.copy(
                        text = "",
                        isLoading = true
                    )
                }
            }
        } catch (e: Exception) {
            state = state.copy(isLoading = false)
            _uiEvent.send(NoteDetailsUiEvent.SnackbarMsg(e.message ?: "Unknown error on ViewModel layer"))
        }
    }
}


data class NoteDetailsState(
    val text: String = "",
    val isLoading: Boolean = false
)

sealed class NoteDetailsAction {
    data class OnTextChanged(val text: String) : NoteDetailsAction()
    object OnClickSave : NoteDetailsAction()

}

sealed class NoteDetailsUiEvent {
    object PopBackStack: NoteDetailsUiEvent()
    data class SnackbarMsg(val msg: String): NoteDetailsUiEvent()
}
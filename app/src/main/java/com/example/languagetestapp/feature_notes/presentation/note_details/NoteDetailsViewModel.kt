package com.example.languagetestapp.feature_notes.presentation.note_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import com.example.languagetestapp.feature_notes.domain.repo.NoteEventRepo
import com.example.languagetestapp.feature_notes.util.Constants.TAG_NOTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val noteEventRepo: NoteEventRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _noteId: String? = null
    private var _noteEdited: Note? = null

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

                    if (_noteId == null) {
                        createNote()
                    } else {
                        updateNote()
                    }
                    _uiEvent.send(NoteDetailsUiEvent.PopBackStack) // todo send with SnackbarMsg
                }
            }
        }
    }

    init {
        _noteId = savedStateHandle.get<String>("noteId")
        _noteId?.let { id ->
            getNoteById(id)
        }
    }

    private fun getNoteById(id: String) = viewModelScope.launch {
        state = state.copy(isLoading = true)
        when (val result = noteRepo.getNoteById(id)) {
            is Resource.Success -> {
                state = state.copy(
                    text = result.data?.text ?: "",
                    isLoading = false
                )
                _noteEdited = result.data
            }
            is Resource.Error -> {
                state = state.copy(
                    text = "",
                    isLoading = false
                )
            }
        }
    }

    private fun createNote() = viewModelScope.launch {
        when (val resp = noteRepo.createNote(state.text)) {
            is Resource.Success -> {
                state = state.copy(isLoading = false)
                noteEventRepo.onNoteCreated(resp.data)
                viewModelScope.launch { // todo remove scope wrapping?
                    _uiEvent.send(
                        NoteDetailsUiEvent.SnackbarMsg(
                            "Note successfully created"
                        )
                    )
                }
            }
            is Resource.Error -> {
                state = state.copy(isLoading = false)
                viewModelScope.launch {
                    _uiEvent.send(
                        NoteDetailsUiEvent.SnackbarMsg(
                            resp.message ?: "Unknown error on ViewModel layer"
                        )
                    )
                }
            }
        }
    }

    private fun updateNote() = viewModelScope.launch {
        when (val resp = noteRepo.updateNote(
            _noteId!!,
            _noteEdited!!.copy(text = state.text)
        )
        ) {
            is Resource.Success -> {
                Log.d(TAG_NOTE, "updateNote.Success: ${resp.data ?: "NULL"}")
                state = state.copy(isLoading = false)
                noteEventRepo.onNoteUpdated(resp.data)
                viewModelScope.launch {
                    _uiEvent.send(
                        NoteDetailsUiEvent.SnackbarMsg(
                            "Note successfully updated"
                        )
                    )
                }
            }
            is Resource.Error -> {
                Log.d(TAG_NOTE, "updateNote.Error: ${resp.data ?: "NULL"}")
                state = state.copy(isLoading = false)
            }
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
    object PopBackStack : NoteDetailsUiEvent()
    data class SnackbarMsg(val msg: String) : NoteDetailsUiEvent()
}
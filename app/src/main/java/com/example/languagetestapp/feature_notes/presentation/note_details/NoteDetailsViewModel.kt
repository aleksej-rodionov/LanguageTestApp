package com.example.languagetestapp.feature_notes.presentation.note_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_notes.di.NoteScope
import com.example.languagetestapp.feature_notes.domain.model.Note
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import com.example.languagetestapp.feature_notes.domain.repo.NoteEventRepo
import com.example.languagetestapp.feature_notes.util.Constants.TAG_NOTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val noteEventRepo: NoteEventRepo,
    @NoteScope private val noteScope: CoroutineScope,
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
                state = state.copy(text = result.data?.text ?: "", isLoading = false)
                _noteEdited = result.data
            }
            is Resource.Error -> {
                state = state.copy(text = "", isLoading = false)
            }
        }
    }

    private fun createNote() = noteScope.launch {
        val resp = noteRepo.createNote(state.text)
        handleResp(resp, true)
    }

    private fun updateNote() = noteScope.launch {
        val resp = noteRepo.updateNote(
            _noteId!!,
            _noteEdited!!.copy(text = state.text)
        )
        handleResp(resp, false)
    }

    private suspend fun handleResp(resp: Resource<Note>, createdNew: Boolean) {
        when (resp) {
            is Resource.Success -> {
                state = state.copy(isLoading = false)
                if (createdNew) noteEventRepo.onNoteCreated(resp.data)
                    else noteEventRepo.onNoteUpdated(resp.data)
                _uiEvent.send(NoteDetailsUiEvent.SnackbarMsg(
                    "Note successfully ${if (createdNew) "created" else "updated"}"
                ))
                _uiEvent.send(NoteDetailsUiEvent.PopBackStack)
            }
            is Resource.Error -> {
                state = state.copy(isLoading = false)
                _uiEvent.send(
                    NoteDetailsUiEvent.SnackbarMsg(
                        resp.message ?: "Unknown error on ViewModel layer"
                    )
                )
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
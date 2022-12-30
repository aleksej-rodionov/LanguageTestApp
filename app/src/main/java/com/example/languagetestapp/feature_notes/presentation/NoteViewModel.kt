package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(NoteActivityState())

    private val _uiEvent = Channel<NoteActivityUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: NoteActivityAction) = viewModelScope.launch {
        when (action) {
            is NoteActivityAction.SearchWidgetStateChanged -> {
                state = state.copy(searchWidgetState = action.state)
            }
            is NoteActivityAction.SearchTextChanged -> {
                state = state.copy(searchText = action.text)
            }
        }
    }

    fun onMetaAction(action: NoteActivityMetaAction) = viewModelScope.launch {
        when (action) {
            is NoteActivityMetaAction.MetaSnackbarMsg -> {
                // todo redirect ALL snackbar messages through this activity ViewModel
                _uiEvent.send(NoteActivityUiEvent.SnackbarMsg(action.msg))
            }
        }
    }
}

data class NoteActivityState(
    val searchWidgetState: SearchWidgetState = SearchWidgetState.Closed,
    val searchText: String = ""
)

sealed class NoteActivityAction {
    data class SearchWidgetStateChanged(val state: SearchWidgetState): NoteActivityAction()
    data class SearchTextChanged(val text: String): NoteActivityAction()
}

sealed class NoteActivityMetaAction {
    data class MetaSnackbarMsg(val msg: String): NoteActivityMetaAction()
}

sealed class NoteActivityUiEvent {
    data class SnackbarMsg(val msg: String): NoteActivityUiEvent()
}
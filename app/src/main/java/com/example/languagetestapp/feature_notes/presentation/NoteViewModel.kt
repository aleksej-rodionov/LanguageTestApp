package com.example.languagetestapp.feature_notes.presentation

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

    private val _uiEvent = Channel<NoteUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onMetaAction(action: NoteMetaAction) = viewModelScope.launch {
        when (action) {
            is NoteMetaAction.MetaSnackbarMsg -> {
                // todo redirect ALL snackbar messages through this activity ViewModel
                _uiEvent.send(NoteUiEvent.SnackbarMsg(action.msg))
            }
        }
    }
}

sealed class NoteMetaAction {
    data class MetaSnackbarMsg(val msg: String): NoteMetaAction()
}

sealed class NoteUiEvent {
    data class SnackbarMsg(val msg: String): NoteUiEvent()
}
package com.example.languagetestapp.feature_profile.presentation.pick_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_notes.presentation.NoteActivityUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickImageViewModel @Inject constructor(

): ViewModel() {

    private val _uiEvent = Channel<PickImageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: PickImageAction) {
        when (action) {
            is PickImageAction.PermDenied -> {
                viewModelScope.launch { _uiEvent.send(PickImageUiEvent.SnackbarMsg("PickFile perm Denied nax!!!!!!!00")) }
                viewModelScope.launch { _uiEvent.send(PickImageUiEvent.PopBackStack) }
            }
        }
    }
}

sealed class PickImageAction {
    object PermDenied: PickImageAction()
}

sealed class PickImageUiEvent {
    data class SnackbarMsg(val msg: String): PickImageUiEvent()
    object PopBackStack: PickImageUiEvent()
}
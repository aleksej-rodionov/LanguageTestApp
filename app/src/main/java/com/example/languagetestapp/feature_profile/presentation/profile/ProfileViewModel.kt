package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_notes.presentation.note_list.NoteListUiEvent
import com.example.languagetestapp.feature_profile.presentation.util.ProfileDest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(ProfileState())

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangePasswordClick -> {
                viewModelScope.launch {
                    _uiEvent.send(ProfileUiEvent.Navigate(ProfileDest.ChangePasswordDest.route))
                }
            }
            is ProfileAction.OnLogoutClick -> {
                viewModelScope.launch { _uiEvent.send(ProfileUiEvent.Logout) }
            }
            is ProfileAction.OnBackClick -> {
                viewModelScope.launch { _uiEvent.send(ProfileUiEvent.PopBackStack) }
            }
            is ProfileAction.OnCamera -> {
                viewModelScope.launch {
                    _uiEvent.send(ProfileUiEvent.Navigate(ProfileDest.CameraDest.route))
                }
            }
            is ProfileAction.OnPickFileImage -> {
                viewModelScope.launch {
                    _uiEvent.send(ProfileUiEvent.Navigate(ProfileDest.PickImageDest.route))
                }
            }
        }
    }
}


data class ProfileState(
    val email: String = "email not found",
    val photoUrl: String? = null,
    val photoUploadProgress: Int? = null
)

sealed class ProfileAction {
    object OnChangePasswordClick: ProfileAction()
    object OnLogoutClick: ProfileAction()
    object OnBackClick: ProfileAction()
    object OnPickFileImage: ProfileAction()
    object OnCamera: ProfileAction()
}

sealed class ProfileUiEvent {
    data class Navigate(val route: String) : ProfileUiEvent()
    object PopBackStack: ProfileUiEvent()
    object Logout: ProfileUiEvent()
    data class SnackbarMsg(val msg: String): ProfileUiEvent()
}
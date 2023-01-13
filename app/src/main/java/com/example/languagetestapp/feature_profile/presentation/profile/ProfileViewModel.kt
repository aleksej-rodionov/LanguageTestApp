package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_profile.presentation.pick_image.IMAGE_SOURCE_CAMERA
import com.example.languagetestapp.feature_profile.presentation.pick_image.IMAGE_SOURCE_FILEPICKER
import com.example.languagetestapp.feature_profile.presentation.util.ProfileDest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepo: AuthRepo
): ViewModel() {

    var state by mutableStateOf(ProfileState())

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        fetchLocalUserData()
    }

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
                    _uiEvent.send(ProfileUiEvent.Navigate(
                        ProfileDest.ChangeAvatarDest.route + "?source=${IMAGE_SOURCE_CAMERA}"
                    ))
                }
            }
            is ProfileAction.OnPickFileImage -> {
                viewModelScope.launch {
                    _uiEvent.send(ProfileUiEvent.Navigate(
                        ProfileDest.ChangeAvatarDest.route + "?source=${IMAGE_SOURCE_FILEPICKER}"
                    ))
                }
            }
        }
    }

    private fun fetchLocalUserData() = viewModelScope.launch(Dispatchers.IO) {
        val user = authRepo.fetchLocalUserData()
        withContext(Dispatchers.Main) {
            state = state.copy(email = user?.email ?: "Email not found", currentImageUrl = user?.avaUrl)
        }
    }
}


data class ProfileState(
    val email: String = "email not found",
    val currentImageUrl: String? = null,
    val newPhotoUrl: String? = null, //todo update this when uploaded
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
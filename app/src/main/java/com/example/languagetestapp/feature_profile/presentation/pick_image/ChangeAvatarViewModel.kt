package com.example.languagetestapp.feature_profile.presentation.pick_image

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.permission.PermissionHandler
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(
    val permissionsHandler: PermissionHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _imageSource: String? = IMAGE_SOURCE_FILEPICKER

    var state by mutableStateOf(State())

    private val _uiEffect = Channel<UiEffect>()
    val uiEvent = _uiEffect.receiveAsFlow()

    init {
        _imageSource = savedStateHandle.get<String>("source")
        state = state.copy(
            imageSourceChosen = _imageSource ?: IMAGE_SOURCE_FILEPICKER
        )

        permissionsHandler.state.onEach { permHandlerState ->
            state = state.copy(
                multiplePermissionsState = permHandlerState.multiplePermissionState
            )
        }.catch {
            _uiEffect.send(
                UiEffect.SnackbarMsg(it.message ?: "Error fetching PermHandler State"))
        }.launchIn(viewModelScope)
    }

    fun onEvent(action: Event) {
        when (action) {
            is Event.PermDenied -> {
                viewModelScope.launch { _uiEffect.send(UiEffect.SnackbarMsg("PickFile perm Denied nax!!!!!!!00")) }
                viewModelScope.launch { _uiEffect.send(UiEffect.PopBackStack) }
            }
            is Event.PermissionRequired -> {
                onPermissionRequired()
            }
        }
    }


    //====================PRIVATE METHODS====================
    private fun onPermissionRequired() {
        permissionsHandler.onEvent(PermissionHandler.Event.PermissionRequired)
    }


    //====================STATE AND EVENT====================
    data class State @OptIn(ExperimentalPermissionsApi::class) constructor(
        val imageSourceChosen: String = IMAGE_SOURCE_FILEPICKER,
        val permissionRequestInFlight: Boolean = false,
        val hasCameraPermission: Boolean = false,
        val multiplePermissionsState: MultiplePermissionsState? = null,
        val permissionAction: PermissionHandler.Action = PermissionHandler.Action.NO_ACTION
    )

    sealed class Event {
        object PermDenied : Event()
        object PermissionRequired : Event()
    }

    sealed class UiEffect {
        data class SnackbarMsg(val msg: String) : UiEffect()
        object PopBackStack : UiEffect()
    }
}
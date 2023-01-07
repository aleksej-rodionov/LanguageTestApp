package com.example.languagetestapp.feature_profile.presentation.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.permission.PermissionHandler
import com.example.languagetestapp.feature_profile.presentation.util.FileManager
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
class CameraViewModel @Inject constructor(
    private val fileManager: FileManager,
    val permissionsHandler: PermissionHandler
) : ViewModel() {

    var state by mutableStateOf(CameraState())

    private val _uiEvent = Channel<CameraUiEffect>()
    val uiEffect = _uiEvent.receiveAsFlow()

    init {
        permissionsHandler.state.onEach { permHandlerState ->
            state = state.copy(multiplePermissionsState = permHandlerState.multiplePermissionState)
        }.catch {
//            viewModelScope.launch {
                _uiEvent.send(CameraUiEffect.SnackbarMsg(it.message ?: "Error fetching PermHandler State"))
//            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.PermDenied -> {
                viewModelScope.launch { _uiEvent.send(CameraUiEffect.SnackbarMsg("Camera Perm Denied nax!!!!!!!00")) }
                viewModelScope.launch { _uiEvent.send(CameraUiEffect.PopBackStack) }
            }
            is CameraEvent.PermissionRequired -> {
                onPermissionRequired()
            }
        }
    }


    //====================PRIVATE METHODS====================
    private fun onPermissionRequired() {
        permissionsHandler.onEvent(PermissionHandler.Event.PermissionRequired)
    }
}

data class CameraState @OptIn(ExperimentalPermissionsApi::class) constructor(
    val permissionRequestInFlight: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val multiplePermissionsState: MultiplePermissionsState? = null,
    val permissionAction: PermissionHandler.Action = PermissionHandler.Action.NO_ACTION
)

sealed class CameraEvent {
    object PermDenied : CameraEvent()
    object PermissionRequired : CameraEvent()
}

sealed class CameraUiEffect {
    data class SnackbarMsg(val msg: String) : CameraUiEffect()
    object PopBackStack : CameraUiEffect()
}
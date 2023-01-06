package com.example.languagetestapp.core.util.permission

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalPermissionsApi::class)
class PermissionHandler {

    private val _state = MutableStateFlow(PermissionState())
    val state: StateFlow<PermissionState> = _state.asStateFlow()

    fun onAction(action: PermissionAction) { //todo rename to onAction?
        when (action) {
            PermissionAction.PermissionDenied -> onPermissionDenied()
            PermissionAction.PermissionDismissTapped -> onPermissionDismissTapped()
            PermissionAction.PermissionNeverAskAgain -> onPermissionNeverShowAgain()
            PermissionAction.PermissionRationaleOkTapped -> onPermissionRationaleOkTapped()
            PermissionAction.PermissionRequired -> onPermissionRequired()
            PermissionAction.PermissionSettingsTapped -> onPermissionSettingsTapped()
            PermissionAction.PermissionsGranted -> onPermissionGranted()
            is PermissionAction.PermissionsStateUpdated -> onPermissionsStateUpdated(action.permissionsState)
        }
    }

    data class PermissionState (
        val multiplePermissionState: MultiplePermissionsState? = null,
        val permissionAction: Action = Action.NO_ACTION
    )

    sealed class PermissionAction {
        object PermissionDenied : PermissionAction()
        object PermissionsGranted : PermissionAction()
        object PermissionSettingsTapped : PermissionAction()
        object PermissionNeverAskAgain : PermissionAction()
        object PermissionDismissTapped : PermissionAction()
        object PermissionRationaleOkTapped : PermissionAction()
        object PermissionRequired : PermissionAction()
        data class PermissionsStateUpdated(val permissionsState: MultiplePermissionsState) :
            PermissionAction()
    }

    enum class Action {
        REQUEST_PERMISSION, SHOW_RATIONALE, SHOW_NEVER_ASK_AGAIN, NO_ACTION
    }



    private fun onPermissionsStateUpdated(permissionState: MultiplePermissionsState) {
        _state.update { it.copy(multiplePermissionState = permissionState) }
    }

    private fun onPermissionGranted() {
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }

    private fun onPermissionDenied() {
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }

    private fun onPermissionNeverShowAgain() {
        _state.update { it.copy(permissionAction = Action.SHOW_NEVER_ASK_AGAIN) }
    }

    private fun onPermissionRequired() {
        _state.value.multiplePermissionState?.let {
            val permissionAction =
                if (!it.allPermissionsGranted && !it.shouldShowRationale && !it.permissionRequested) {
                    Action.REQUEST_PERMISSION
                } else if (!it.allPermissionsGranted && it.shouldShowRationale) {
                    Action.SHOW_RATIONALE
                } else {
                    Action.SHOW_NEVER_ASK_AGAIN
                }
            _state.update { it.copy(permissionAction = permissionAction) }
        }
    }

    private fun onPermissionRationaleOkTapped() {
        _state.update { it.copy(permissionAction = Action.REQUEST_PERMISSION) }
    }

    private fun onPermissionDismissTapped() {
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }

    private fun onPermissionSettingsTapped() {
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }
}
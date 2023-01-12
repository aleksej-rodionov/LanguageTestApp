package com.example.languagetestapp.core.util.permission

import android.util.Log
import com.example.languagetestapp.feature_profile.presentation.util.Constants.TAG_PERMIT
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

    fun onEvent(action: Event) { //todo rename to onAction?
        when (action) {
            Event.PermissionDenied -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionDenied")
                onPermissionDenied()
            }
            Event.PermissionDismissTapped -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionDismissTapped")
                onPermissionDismissTapped()
            }
            Event.PermissionNeverAskAgain -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionNeverAskAgain")
                onPermissionNeverShowAgain()
            }
            Event.PermissionRationaleOkTapped -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionRationaleOkTapped")
                onPermissionRationaleOkTapped()
            }
            Event.PermissionRequired -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionRequired")
                onPermissionRequired()
            }
            Event.PermissionSettingsTapped -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionSettingsTapped")
                onPermissionSettingsTapped()
            }
            Event.PermissionsGranted -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionsGranted")
                onPermissionGranted()
            }
            is Event.PermissionsStateUpdated -> {
                Log.d(TAG_PERMIT, "onEvent: PermissionsStateUpdated")
                onPermissionsStateUpdated(action.permissionsState)
            }
        }
    }

    data class PermissionState(
        val multiplePermissionState: MultiplePermissionsState? = null,
        val permissionAction: Action = Action.NO_ACTION
    )

    sealed class Event {
        object PermissionDenied : Event()
        object PermissionsGranted : Event()
        object PermissionSettingsTapped : Event()
        object PermissionNeverAskAgain : Event()
        object PermissionDismissTapped : Event()
        object PermissionRationaleOkTapped : Event()
        object PermissionRequired : Event()
        data class PermissionsStateUpdated(val permissionsState: MultiplePermissionsState) :
            Event()
    }

    enum class Action {
        REQUEST_PERMISSION, SHOW_RATIONALE, SHOW_NEVER_ASK_AGAIN, NO_ACTION
    }


    private fun onPermissionsStateUpdated(permissionState: MultiplePermissionsState) {
//        Log.d(TAG_PERMIT, "onPermissionsStateUpdated: CALLED")
        _state.update { it.copy(multiplePermissionState = permissionState) }
    }

    private fun onPermissionGranted() {
//        Log.d(TAG_PERMIT, "onPermissionGranted: CALLED")
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }

    private fun onPermissionDenied() {
//        Log.d(TAG_PERMIT, "onPermissionDenied: CALLED ")
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }

    private fun onPermissionNeverShowAgain() {
//        Log.d(TAG_PERMIT, "onPermissionNeverShowAgain: CALLED ")
        _state.update { it.copy(permissionAction = Action.SHOW_NEVER_ASK_AGAIN) }
    }

    private fun onPermissionRequired() {
        Log.d("TAG_PERMIT_ACT_REQ", "onPermissionRequired: CALLED ")
        _state.value.multiplePermissionState?.let {
            val permissionAction =
                if (!it.allPermissionsGranted && !it.shouldShowRationale && !it.permissionRequested) {
                    Log.d("TAG_PERMIT_ACT_REQ", "onPermissionRequired: Action.REQUEST_PERMISSION")
                    Action.REQUEST_PERMISSION
                } else if (!it.allPermissionsGranted && it.shouldShowRationale) {
//                    Log.d(TAG_PERMIT, "onPermissionRequired: ")
                    Action.SHOW_RATIONALE
                } else {
//                    Log.d(TAG_PERMIT, "onPermissionRequired: ")
                    Action.SHOW_NEVER_ASK_AGAIN
                }
            _state.update { it.copy(permissionAction = permissionAction) }
        }
    }

    private fun onPermissionRationaleOkTapped() { //todo here is bug?
        Log.d("TAG_PERMIT_ACT_REQ", "onPermissionRationaleOkTapped: CALLED")
//        _state.update { it.copy(permissionAction = Action.REQUEST_PERMISSION) }
        _state.update { it.copy(permissionAction = Action.REQUEST_PERMISSION) }
    }

    private fun onPermissionDismissTapped() {
//        Log.d(TAG_PERMIT, "onPermissionDismissTapped: CALLED")
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }

    private fun onPermissionSettingsTapped() {
//        Log.d(TAG_PERMIT, "onPermissionSettingsTapped: CALLED")
        _state.update { it.copy(permissionAction = Action.NO_ACTION) }
    }
}
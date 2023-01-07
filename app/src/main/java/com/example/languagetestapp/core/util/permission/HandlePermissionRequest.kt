package com.example.languagetestapp.core.util.permission

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.languagetestapp.R
import com.example.languagetestapp.feature_profile.presentation.util.Constants.TAG_PERMIT
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionsRequest(
    permissions: List<String>,
    permissionHandler: PermissionHandler
) {

    Log.d(TAG_PERMIT, "HandlePermissionsRequest: permissions = $permissions")

    val state by permissionHandler.state.collectAsState()
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

//    Log.d(TAG_PERMIT, "HandlePermissionsRequest: rememberMultiPermState = ${permissionState}")

    LaunchedEffect(permissionState) { // todo learn the rules of passing 1st argument
        permissionHandler.onEvent(PermissionHandler.Event.PermissionsStateUpdated(permissionState))
        when {
            permissionState.allPermissionsGranted -> {
                permissionHandler.onEvent(PermissionHandler.Event.PermissionsGranted)
            }
            permissionState.permissionRequested -> {
                permissionHandler.onEvent(PermissionHandler.Event.PermissionNeverAskAgain)
            }
            else -> {
                permissionHandler.onEvent(PermissionHandler.Event.PermissionDenied)
            }
        }
    }

    HandlePermissionAction(
        action = state.permissionAction,
        permissionStates = state.multiplePermissionState,
        rationaleText = R.string.permission_rationale,
        neverAskAgainText = R.string.permission_rationale,
        onOkTapped = {
            permissionHandler.onEvent(PermissionHandler.Event.PermissionRationaleOkTapped)
        },
        onSettingsTapped = {
            permissionHandler.onEvent(PermissionHandler.Event.PermissionSettingsTapped)
        }
    )
}
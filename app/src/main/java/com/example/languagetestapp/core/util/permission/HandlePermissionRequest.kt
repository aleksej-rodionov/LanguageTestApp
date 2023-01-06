package com.example.languagetestapp.core.util.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.languagetestapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionsRequest(
    permissions: List<String>,
    permissionHandler: PermissionHandler
) {

    val state by permissionHandler.state.collectAsState()
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    LaunchedEffect(permissionState) { // todo learn the rules of passing 1st argument
        permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionsStateUpdated(permissionState))
        when {
            permissionState.allPermissionsGranted -> {
                permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionsGranted)
            }
            permissionState.permissionRequested -> {
                permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionNeverAskAgain)
            }
            else -> {
                permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionDenied)
            }
        }
    }

    HandlePermissionAction(
        action = state.permissionAction,
        permissionStates = state.multiplePermissionState,
        rationaleText = R.string.permission_rationale,
        neverAskAgainText = R.string.permission_rationale,
        onOkTapped = {
            permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionRationaleOkTapped)
        },
        onSettingsTapped = {
            permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionSettingsTapped)
        }
    )
}
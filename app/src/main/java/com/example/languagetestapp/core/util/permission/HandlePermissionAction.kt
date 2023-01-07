package com.example.languagetestapp.core.util.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.languagetestapp.R
import com.example.languagetestapp.feature_profile.presentation.util.Constants.TAG_PERMIT
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandlePermissionAction(
    action: PermissionHandler.Action,
    permissionStates: MultiplePermissionsState?,
    @StringRes rationaleText: Int,
    @StringRes neverAskAgainText: Int,
    onOkTapped: () -> Unit,
    onSettingsTapped: () -> Unit,
) {

    val context = LocalContext.current

    when (action) {
        PermissionHandler.Action.REQUEST_PERMISSION -> {
//            permissionStates?.let {
                LaunchedEffect(true) {
                    permissionStates?.apply {
                      Log.d("TAG_PERMIT_ACT_REQ", "permissions = $permissions")
                      Log.d("TAG_PERMIT_ACT_REQ", "revokedPermissions = $revokedPermissions")
                      Log.d("TAG_PERMIT_ACT_REQ", "allPermissionsGranted = $allPermissionsGranted")
                      Log.d("TAG_PERMIT_ACT_REQ", "shouldShowRationale = $shouldShowRationale")
                      Log.d("TAG_PERMIT_ACT_REQ", "permissionRequested = $permissionRequested")
                    }


                    /**
                     * TODO
                     * This should always be triggered from non-composable scope,
                     * for example, from a side-effect or a non-composable callback.
                     * Otherwise, this will result in an IllegalStateException.
                     */
                    permissionStates?.launchMultiplePermissionRequest()
                }
//            }
        }
        PermissionHandler.Action.SHOW_RATIONALE -> {
            PermissionRationaleDialog(
                message = stringResource(rationaleText),
                onOkTapped = onOkTapped
            )
        }
        PermissionHandler.Action.SHOW_NEVER_ASK_AGAIN -> {
            ShowGotoSettingsDialog(
                title = stringResource(R.string.allow_permission),
                message = stringResource(neverAskAgainText),
                onSettingsTapped = {
                    onSettingsTapped()
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:" + context.packageName)
                        context.startActivity(this)
                    }
                },
            )
        }
        PermissionHandler.Action.NO_ACTION -> Unit
    }
}
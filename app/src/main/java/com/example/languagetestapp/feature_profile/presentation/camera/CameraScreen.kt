package com.example.languagetestapp.feature_profile.presentation.camera

import android.Manifest
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.feature_profile.presentation.camera.ui_elements.CameraScreenContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    cameraViewModel: CameraViewModel = hiltViewModel()
) {

    val state = cameraViewModel.state

    val permissions = remember { listOf(Manifest.permission.CAMERA) }
    HandlePermissionsRequest(
        permissions = permissions,
        permissionHandler = cameraViewModel.permissionsHandler
    )

    LaunchedEffect(key1 = true) {
        cameraViewModel.uiEffect.collect { effect ->
            when (effect) {
                is CameraUiEffect.PopBackStack -> { onPopBackStack() }
                is CameraUiEffect.SnackbarMsg -> { showSnackbar(effect.msg, SnackbarDuration.Short) }
            }
        }
    }

//    CompositionLocalProvider("pizda") {

    CameraScreenContent(
        allPermissionsGranted = state.multiplePermissionsState?.allPermissionsGranted ?: false,
        onEvent = cameraViewModel::onEvent
    )

//    }
}
















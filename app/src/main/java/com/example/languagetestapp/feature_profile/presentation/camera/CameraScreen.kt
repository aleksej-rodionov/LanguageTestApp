package com.example.languagetestapp.feature_profile.presentation.camera

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.feature_profile.presentation.camera.ui_elements.CameraScreenContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    cameraViewModel: CameraViewModel = hiltViewModel()
) {


    val state = cameraViewModel.state
    val scope = rememberCoroutineScope()

    val permissions = remember { listOf(Manifest.permission.CAMERA) }
    HandlePermissionsRequest(
        permissions = permissions,
        permissionHandler = cameraViewModel.permissionsHandler
    )

    LaunchedEffect(key1 = true) {
        cameraViewModel.uiEffect.collect { effect ->
            when (effect) {
                is CameraUiEffect.PopBackStack -> { onPopBackStack() }
                is CameraUiEffect.SnackbarMsg -> {
                    showSnackbar(effect.msg, SnackbarDuration.Short) }
            }
        }
    }

//    CompositionLocalProvider("pizda") {

//    CameraScreenContent(
//        allPermissionsGranted = state.multiplePermissionsState?.allPermissionsGranted ?: false,
//        onEvent = cameraViewModel::onEvent
//    )

    val allPermissionsGranted = //todo при возвращении перепроверять это говно (аналог onResume)
        state.multiplePermissionsState?.allPermissionsGranted ?: false
    // todo trigger it in VM??

    Log.d("TAG_PERMIT_ACT_REQ", "CameraScreen: allPermissionsGranted = $allPermissionsGranted")

    if (!allPermissionsGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                cameraViewModel.onEvent(CameraEvent.PermissionRequired) //todo launch this fun immediately without diwplaaaaaying this stupid Button
            }) {
                Text(text = "Nenuzhnaya knopka \nkotoruyu potom nada udalit\'\ni srazu zapuskat\' onEvent()")
            }
        }
    } else {

        CameraScreenContent(/*allPermissionsGranted = , onEvent = */)
    }

//    }
}
















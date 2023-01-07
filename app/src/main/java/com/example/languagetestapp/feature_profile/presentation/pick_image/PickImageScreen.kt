package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.di.ApplicationScope
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.feature_profile.presentation.camera.CameraEvent
import com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements.PickImageScreenContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickImageScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: PickImageViewModel = hiltViewModel()
) {


    val state = viewModel.state

    val permissions = remember { listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) }
    HandlePermissionsRequest(
        permissions = permissions,
        permissionHandler = viewModel.permissionsHandler
    )

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collect { effect ->
            when (effect) {
                is PickImageViewModel.UiEffect.PopBackStack -> {
                    onPopBackStack()
                }
                is PickImageViewModel.UiEffect.SnackbarMsg -> {
                    showSnackbar(effect.msg, SnackbarDuration.Short)
                }
            }
        }
    })

    //    CompositionLocalProvider("pizda") {

    val allPermissionsGranted =
        state.multiplePermissionsState?.allPermissionsGranted ?: false
    if (!allPermissionsGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                viewModel.onEvent(PickImageViewModel.Event.PermissionRequired) //todo launch this fun immediately without diwplaaaaaying this stupid Button
            }) {
                Text(text = "Nenuzhnaya knopka \nkotoruyu potom nada udalit\'\ni srazu zapuskat\' onEvent()")
            }
        }
    } else {

        PickImageScreenContent()
    }

//    }
}
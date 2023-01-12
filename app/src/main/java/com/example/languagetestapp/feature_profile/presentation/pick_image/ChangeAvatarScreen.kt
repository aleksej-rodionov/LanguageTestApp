package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.Manifest
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.feature_profile.presentation.camera.ui_elements.CameraScreenContent
import com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements.ChangeAvatarContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChangeAvatarScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: ChangeAvatarViewModel = hiltViewModel()
) {

    val state = viewModel.state

    val permissions = remember {
        //todo replace with sealed class
        when (state.imageSourceChosen) {
            IMAGE_SOURCE_FILEPICKER -> {
                listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            IMAGE_SOURCE_CAMERA -> {
                listOf(Manifest.permission.CAMERA)
            }
            else -> {
                listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }



    HandlePermissionsRequest(
        permissions = permissions,
        permissionHandler = viewModel.permissionsHandler
    )

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collect { effect ->
            when (effect) {
                is ChangeAvatarViewModel.UiEffect.PopBackStack -> {
                    onPopBackStack()
                }
                is ChangeAvatarViewModel.UiEffect.SnackbarMsg -> {
                    showSnackbar(effect.msg, SnackbarDuration.Short)
                }
            }
        }
    })

//    CompositionLocalProvider("blabla") {

    val allPermissionsGranted =
        state.multiplePermissionsState?.allPermissionsGranted ?: false

    if (!allPermissionsGranted) {
        viewModel.onEvent(ChangeAvatarViewModel.Event.PermissionRequired)
    } else {
        ChangeAvatarContent(state.imageSourceChosen)
    }

//    }
}

const val IMAGE_SOURCE_CAMERA = "camera"
const val IMAGE_SOURCE_FILEPICKER = "filepicker"
package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements.ChangeAvatarContent
import com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements.ChangeAvatarContentViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChangeAvatarScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: ChangeAvatarViewModel = hiltViewModel()
) {

    val state = viewModel.state

//    val launchSourceHandled = remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(ChangeAvatarViewModel.Event.OnImageSelected(it))
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                viewModel.onEvent(ChangeAvatarViewModel.Event.OnCameraSuccess)
            }
        }
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

                is ChangeAvatarViewModel.UiEffect.LaunchPicker -> {
                    imagePicker.launch("image/*")
                }
                is ChangeAvatarViewModel.UiEffect.LaunchCameraWithUri -> {
                    cameraLauncher.launch(effect.uri)
                }
            }
        }
    })

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

//    CompositionLocalProvider("blabla") {

    val allPermissionsGranted =
        state.multiplePermissionsState?.allPermissionsGranted ?: false

    if (!allPermissionsGranted) {
        viewModel.onEvent(ChangeAvatarViewModel.Event.PermissionRequired)
    } else {

        if (!state.launchSourceHandled) {
//            launchSourceHandled.value = true
            viewModel.onEvent(ChangeAvatarViewModel.Event.LaunchSourceHandled)

            when (state.imageSourceChosen) {
                IMAGE_SOURCE_FILEPICKER -> {
                    viewModel.onEvent(ChangeAvatarViewModel.Event.LaunchImagePicker)
                }
                IMAGE_SOURCE_CAMERA -> {
                    viewModel.onEvent(ChangeAvatarViewModel.Event.LaunchCamera)
                }
                else -> { // todo get rid of it by using sealed
                    viewModel.onEvent(ChangeAvatarViewModel.Event.LaunchImagePicker)
                }
            }
        }

        with(state) {
            ChangeAvatarContent(
                imageSourceChosen,
                uploadPercentage,
                localImageUri,
                remoteImageUrl,
                hasImage,
                { viewModel.onEvent(ChangeAvatarViewModel.Event.Submit) }
//                showSnackbar
            )
        }
    }

//    }
}

const val IMAGE_SOURCE_CAMERA = "camera"
const val IMAGE_SOURCE_FILEPICKER = "filepicker"
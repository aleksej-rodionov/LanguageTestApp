package com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.languagetestapp.R
import com.example.languagetestapp.feature_file.util.CustomFileProvider
import com.example.languagetestapp.feature_profile.presentation.pick_image.IMAGE_SOURCE_CAMERA
import com.example.languagetestapp.feature_profile.presentation.pick_image.IMAGE_SOURCE_FILEPICKER

@Composable
fun ChangeAvatarContent(
    imageSource: String,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: ChangeAvatarContentViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = viewModel.state

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // todo pass through onEvent()
            viewModel.state = viewModel.state.copy(hasImage = uri != null)
            uri?.let {
                viewModel.onImageSelected(it)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            // todo pass through onEvent()
            viewModel.state = viewModel.state.copy(hasImage = isSuccess)
            viewModel.executeUploadFromLocalStorageToBodyPart()
        }
    )

    when (imageSource) {
        IMAGE_SOURCE_FILEPICKER -> {
            // todo pass through onEvent()
//            imagePicker.launch("image/*")
            viewModel.onEvent(ChangeAvatarContentViewModel.Event.LaunchImagePicker)
        }
        IMAGE_SOURCE_CAMERA -> {
            // todo pass through onEvent()
            viewModel.onEvent(ChangeAvatarContentViewModel.Event.LaunchCamera)
        }
        else -> { // todo get rid of it by using sealed
//            imagePicker.launch("image/*")
            viewModel.onEvent(ChangeAvatarContentViewModel.Event.LaunchImagePicker)
        }
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ChangeAvatarContentViewModel.UiEffect.SnackbarMsg -> {
                    showSnackbar(effect.msg, SnackbarDuration.Short)
                }
                is ChangeAvatarContentViewModel.UiEffect.LaunchPicker -> {
                    imagePicker.launch("image/*")
                }
                is ChangeAvatarContentViewModel.UiEffect.LaunchCameraWithUri -> {
                    cameraLauncher.launch(effect.uri)
                }
            }
        }
    })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.hasImage && state.remoteImageUrl?.isNotEmpty() == true) {
                AsyncImage(
                    model = state.remoteImageUrl,
                    contentDescription = null,
                    error = painterResource(R.drawable.ava_placeholder),
                    modifier = Modifier
                        .width(250.dp)
                        .height(250.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ava_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .width(250.dp)
                        .height(250.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = ("localImgUri = " + state.localImageUri?.let { "$it" }) ?: "empty")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = if (state.remoteImageUrl.isNullOrBlank()) {
                state.uploadPercentage?.let { "$it%" } ?: ""
            } else "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = ("remoteImgUri = " + state.remoteImageUrl?.let { "$it" }) ?: "empty")
            Spacer(modifier = Modifier.height(16.dp))
//            Button(
//                onClick = {
//                    when (imageSource) {
//                        IMAGE_SOURCE_FILEPICKER -> {
//                            imagePicker.launch("image/*")
//                        }
//                        IMAGE_SOURCE_CAMERA -> {
//                            // todo pass through onEvent()
//                            val uri = CustomFileProvider.getImageUri(context)
//                            viewModel.state = viewModel.state.copy(hasImage = false) //todo remove?
//                            viewModel.state = viewModel.state.copy(localImageUri = uri.toString())
//                            cameraLauncher.launch(uri)
//                        }
//                        else -> { // todo get rid of it by using sealed
//                            imagePicker.launch("image/*")
//                        }
//                    }
//                }
//            ) {
//                Text(text = "Click, but remove btn later pls")
//            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.updateUserWithNewAva()
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}
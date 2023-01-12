package com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
    viewModel: PickImageContentViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = viewModel.state

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            viewModel.state = viewModel.state.copy(hasImage = uri != null) // todo pass through onEvent()
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
            viewModel.executeUploadingBytes()
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // todo if(hasImage) check
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

            Text(text = if (state.remoteImageUrl.isNullOrBlank()) {
                state.uploadPercentage?.let { "$it%" } ?: ""
            } else "")
            Text(text = state.remoteImageUrl?.let { "$it" } ?: "empty")
            Button(
                onClick = {
                    when (imageSource) {
                        IMAGE_SOURCE_FILEPICKER -> {
                            imagePicker.launch("image/*")
                        }
                        IMAGE_SOURCE_CAMERA -> {
                            val uri = CustomFileProvider.getImageUri(context)
                            // todo pass through onEvent()
                            viewModel.state = viewModel.state.copy(hasImage = false) //todo remove?
                            viewModel.state = viewModel.state.copy(localImageUri = uri.toString())
                            cameraLauncher.launch(uri)
                        }
                        else -> { // todo get rid of using sealed
                            imagePicker.launch("image/*")
                        }
                    }
                }
            ) {
                Text(text = "Click, but remove btn later pls")
            }
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
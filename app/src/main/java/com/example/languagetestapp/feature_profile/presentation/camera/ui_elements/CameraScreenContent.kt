package com.example.languagetestapp.feature_profile.presentation.camera.ui_elements

import android.util.Log
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
import com.example.languagetestapp.feature_file.util.Constants.TAG_IMAGE
import com.example.languagetestapp.feature_file.util.CustomFileProvider

@Composable
fun CameraScreenContent(
    viewModel: CameraContentViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = viewModel.state

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            // todo pass through onEvent()
            viewModel.state = viewModel.state.copy(hasImage = isSuccess)
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

            Log.d(TAG_IMAGE, "Camera. State = $state")

            if (state.hasImage && state.imageUri?.isNotEmpty() == true) {
                AsyncImage(
                    model = state.imageUri,
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

            Button(
                onClick = {
                    val uri = CustomFileProvider.getImageUri(context)
                    // todo pass through onEvent()
                    viewModel.state = viewModel.state.copy(hasImage = false) //todo remove?
                    viewModel.state = viewModel.state.copy(imageUri = uri.toString())
                    cameraLauncher.launch(uri)
                }
            ) {
                Text(text = "Click, but remove btn later pls")
            }
        }
    }
}
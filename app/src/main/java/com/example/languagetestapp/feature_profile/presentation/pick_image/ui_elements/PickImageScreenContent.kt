package com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.languagetestapp.R
import com.example.languagetestapp.feature_file.util.Constants.TAG_IMAGE
import com.example.languagetestapp.feature_profile.presentation.pick_image.PickImageContentViewModel

@Composable
fun PickImageScreenContent(
    viewModel: PickImageContentViewModel = hiltViewModel()
) {

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
                    imagePicker.launch("image/*")
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
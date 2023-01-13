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
    uploadPercentage: Int? = null,
    localImageUri: String? = null,
    remoteImageUrl: String? = null,
    hasImage: Boolean = false,
    onSubmitClick: () -> Unit,
    viewModel: ChangeAvatarContentViewModel = hiltViewModel()
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (hasImage && (remoteImageUrl?.isNotEmpty() == true || localImageUri?.isNotEmpty() == true )) {
                AsyncImage(
                    model = remoteImageUrl ?: localImageUri,
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
            Text(text = ("localImgUri = " + localImageUri?.let { "$it" }) ?: "empty")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = if (remoteImageUrl.isNullOrBlank()) {
                uploadPercentage?.let { "$it%" } ?: ""
            } else "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = ("remoteImgUri = " + remoteImageUrl?.let { "$it" }) ?: "empty")
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
//                    viewModel.onEvent(ChangeAvatarContentViewModel.Event.Submit)
                    onSubmitClick()
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}
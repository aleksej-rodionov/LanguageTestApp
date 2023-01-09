package com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.feature_file.util.Constants.TAG_FILE
import com.example.languagetestapp.feature_profile.presentation.pick_image.PickImageContentViewModel

@Composable
fun PickImageScreenContent(
    viewModel: PickImageContentViewModel = hiltViewModel()
) {

    val state = viewModel.state

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                Log.d(TAG_FILE, "Picked image external url = $it")
                //todo prepare to upload. Note that it is external uri?

                viewModel.onImageSelected(it)
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(text = if (state.remoteUrl.isNullOrBlank()) {
                state.uploadPercentage?.let { "$it%" } ?: ""
            } else "")
            Text(text = state.remoteUrl?.let { "$it" } ?: "empty")
            Button(
                onClick = {
                    imagePicker.launch("image/*")
                }
            ) {
                Text(text = "Click, but remove btn later pls")
            }
        }
    }
}
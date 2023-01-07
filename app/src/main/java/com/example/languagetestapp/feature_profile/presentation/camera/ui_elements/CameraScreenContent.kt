package com.example.languagetestapp.feature_profile.presentation.camera.ui_elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.languagetestapp.feature_profile.presentation.camera.CameraEvent
import com.example.languagetestapp.feature_profile.presentation.camera.CameraViewModel

@Composable
fun CameraScreenContent(
    allPermissionsGranted: Boolean,
    onEvent: (CameraEvent) -> Unit
) {
    if (!allPermissionsGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                onEvent(CameraEvent.PermissionRequired)//todo launch this fun immediately without diwplaaaaaying this stupid Button
            }) {
                Text(text = "Nenuzhnaya knopka \nkotoruyu potom nada udalit\'\ni srazu zapuskat\' onEvent()")
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "Here gonna be camera launched",
                modifier = Modifier.align(Alignment.Center)
            )

        }
    }
}
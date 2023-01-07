package com.example.languagetestapp.feature_profile.presentation.camera.ui_elements

import android.util.Log
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
fun CameraScreenContent() {

    Log.d("TAG_PERMIT_ACT_REQ", "CameraScreenContent: CALLED")

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Here gonna be camera launched",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
package com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PickImageScreenContent() {

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Here gonns be filePicking func",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
package com.example.languagetestapp.feature_profile.presentation.pick_image

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PickImageScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: PickImageViewModel = hiltViewModel()
) {


}
package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.R

@Composable
fun ProfileScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    popBackStack: () -> Unit,
    logout: () -> Unit,
    onNavigate: (ProfileUiEvent.Navigate) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.Navigate -> {
                    onNavigate(event)
                }
                is ProfileUiEvent.PopBackStack -> {
                    popBackStack()
                }
                is ProfileUiEvent.Logout -> {
                    logout()
                }
                is ProfileUiEvent.SnackbarMsg -> {
                    showSnackbar(event.msg, SnackbarDuration.Short)
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        ProfileDetail(
            state = state,
            onBackClick = { viewModel.onAction(ProfileAction.OnBackClick) },
            onLogoutClick = { viewModel.onAction(ProfileAction.OnLogoutClick) }
        )
    }
}
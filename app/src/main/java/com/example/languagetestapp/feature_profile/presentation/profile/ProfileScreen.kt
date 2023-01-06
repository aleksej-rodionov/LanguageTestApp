package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    popBackStack: () -> Unit,
    logout: () -> Unit,
    onNavigate: (ProfileUiEvent.Navigate) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

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

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ProfileBar(
                title = ""/*"Profile"*/,
                onBackClick = {
                    viewModel.onAction(ProfileAction.OnBackClick)
                },
                onLogoutClick = {
                    viewModel.onAction(ProfileAction.OnLogoutClick)
                }
            ) }
    ) { pv ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv),
            color = MaterialTheme.colors.background
        ) {

            ProfileDetail(
                state = state,
                onChangePasswordClick = {
                    viewModel.onAction(ProfileAction.OnChangePasswordClick)
                }
            )
        }
    }
}






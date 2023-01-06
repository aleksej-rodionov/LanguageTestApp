package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.languagetestapp.feature_notes.util.Constants
import com.example.languagetestapp.feature_profile.presentation.util.ProfileDest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun NoteMainComposable(
    toAuthActivity: () -> Unit,
    logout: () -> Unit,
    noteViewModel: NoteViewModel
) {
    val state = noteViewModel.state
    val noteMainState: NoteMainState = rememberNoteSnackbarState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        noteViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is NoteActivityUiEvent.SnackbarMsg -> {
                    noteMainState.showSnackbar(event.msg, SnackbarDuration.Short)
                }
                is NoteActivityUiEvent.ToAuthActivity -> {
                    toAuthActivity()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = noteMainState.scaffoldState,
        drawerGesturesEnabled = true,
        drawerContent = {
            NoteDrawerBody(
                email = state.userEmail,
                toProfileClick = {
                    noteMainState.navController.navigate(ProfileDest.ProfileDestination.route)
                    scope.launch { noteMainState.scaffoldState.drawerState.close() }
                }
            )
        }
    ) { pv ->

        Box(
            modifier = Modifier.padding(pv)
        ) {

            NoteNavigation(
                navController = noteMainState.navController,
                showSnackbar = { msg, dur ->
                    noteMainState.showSnackbar(msg, dur)
                },
                openDrawerClick = {
                    scope.launch {
                        // Open the drawer with animation
                        // and suspend until it is fully
                        // opened or animation has been canceled
                        noteMainState.scaffoldState.drawerState.open()
                    }
                },
                logout = {
                    logout()
                }
            )
        }
    }
}
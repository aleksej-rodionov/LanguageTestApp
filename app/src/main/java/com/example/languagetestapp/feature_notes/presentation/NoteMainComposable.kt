package com.example.languagetestapp.feature_notes.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest
import com.example.languagetestapp.feature_notes.util.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun NoteMainComposable(
    toAuthActivity: () -> Unit,
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
        topBar = {
            NoteTopBar(
                state = state,
                onTextChange = {
                    noteViewModel.onAction(NoteActivityAction.SearchTextChanged(it))
                },
                onCloseClick = {
                    noteViewModel.onAction(NoteActivityAction.SearchTextChanged(""))
                    noteViewModel.onAction(NoteActivityAction.SearchWidgetStateChanged(SearchWidgetState.Closed))
                },
                onSearchClick = {
                    Log.d(Constants.TAG_SEARCH, "searchText = $it")
                },
                onSearchTriggered = {
                    noteViewModel.onAction(NoteActivityAction.SearchWidgetStateChanged(SearchWidgetState.Opened))
                },
                onOpenDrawerClick = {
                    scope.launch {
                        // Open the drawer with animation
                        // and suspend until it is fully
                        // opened or animation has been canceled
                        noteMainState.scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = true,
        drawerContent = {
            NoteDrawerBody(
                email = state.userEmail,
                onLogoutCLick = {
                    noteViewModel.onAction(NoteActivityAction.OnLogout)
                    scope.launch { noteMainState.scaffoldState.drawerState.close() }
                },
                onChangePasswordClick = {
                    noteMainState.navController.navigate(NoteDest.ChangePasswordDest.route)
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
                }
            )
        }
    }
}
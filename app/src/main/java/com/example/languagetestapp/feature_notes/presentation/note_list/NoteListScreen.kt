package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.feature_notes.domain.model.Note

@Composable
fun NoteListScreen(
    onNavigate: (NoteListUiEvent.Navigate) -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is NoteListUiEvent.Navigate -> {
                    // todo
                }
                is NoteListUiEvent.SnackbarMsg -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.msg)
                }
            }
        }
    })

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onAction(NoteListAction.OnCreateNoteClick)
            }) {

            }
        }
    ) {

    }
}


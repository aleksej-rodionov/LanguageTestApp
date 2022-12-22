package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    ) { pv ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(pv)
                    .fillMaxSize()
            ) {
                items(state.notes.size) { index ->
                    val note = state.notes[index]
                    if (index > 0) {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    // todo NoteItem(note = note)
                    if (index < state.notes.size - 1) {
                        Divider()
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


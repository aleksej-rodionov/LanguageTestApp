package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
                    onNavigate(event)
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
            FloatingActionButton(
                modifier = Modifier.padding(32.dp),
                onClick = {
                    viewModel.onAction(NoteListAction.OnCreateNoteClick)
                }
            ) {

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
                    NoteItem(
                        note = note,
                        onAction = viewModel::onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // todo viewModel.onAction(NoteListAction.OnNoteClick(note))
                            }
                            .padding(16.dp)
                    )
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


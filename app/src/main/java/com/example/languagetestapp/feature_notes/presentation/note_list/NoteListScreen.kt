package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteListScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onNavigate: (NoteListUiEvent.Navigate) -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val pullRefreshState = rememberPullRefreshState(
        state.isRefreshing,
        { viewModel.onPullRefresh() }
    )

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is NoteListUiEvent.Navigate -> {
                    onNavigate(event)
                }
                is NoteListUiEvent.SnackbarMsg -> {
                    showSnackbar(event.msg, SnackbarDuration.Short)
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
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { pv ->

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .pullRefresh(pullRefreshState)
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
                                viewModel.onAction(NoteListAction.OnNoteClick(note))
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

            PullRefreshIndicator(
                state.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


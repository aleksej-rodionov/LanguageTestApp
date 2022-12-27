package com.example.languagetestapp.feature_notes.presentation.note_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteDetailsScreen(
    onPopBackStack: () -> Unit, // todo navigateBackWIthSnackbarMsg
    viewModel: NoteDetailsViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is NoteDetailsUiEvent.SnackbarMsg -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.msg)
                }
                is NoteDetailsUiEvent.PopBackStack -> {
                    onPopBackStack()
                }
            }
        }
    })

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { pv ->

        Box(
            modifier = Modifier.padding(pv)
        ) {

            TextField(
                value = state.text,
                onValueChange = {
                    viewModel.onAction(NoteDetailsAction.OnTextChanged(it))
                },
                placeholder = {
                    Text(text = "Description")
                },
                singleLine = false,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize()
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
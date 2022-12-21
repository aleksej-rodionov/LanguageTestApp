package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteListScreen(
    onNavigate: (NoteListUiEvent.Navigate) -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {

}
package com.example.languagetestapp.feature_notes.presentation.note_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(

): ViewModel() {


}

sealed class NoteListUiEvent {
    data class Navigate(val route: String): NoteListUiEvent()
}
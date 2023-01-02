package com.example.languagetestapp.feature_notes.presentation.util

sealed class NoteDest(val route: String) {
    object NoteListDest: NoteDest(route = "noteList")
    object NoteDetailsDest: NoteDest(route = "noteDetails")
    object ChangePasswordDest: NoteDest(route = "changePassword")
}

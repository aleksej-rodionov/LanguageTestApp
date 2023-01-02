package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.languagetestapp.feature_notes.presentation.note_details.NoteDetailsScreen
import com.example.languagetestapp.feature_notes.presentation.note_list.NoteListScreen
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest

@Composable
fun NoteNavigation(
    navController: NavHostController,
    //this Composable need to pass the showSnackbar function on to the screens
    showSnackbar: (String, SnackbarDuration) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = NoteDest.NoteListDest.route,
        builder = {

            composable(
                route = NoteDest.NoteListDest.route
            ) {
                NoteListScreen(
                    showSnackbar = showSnackbar,
                    onNavigate = {
                        navController.navigate(it.route)
                    }
                )
            }

            composable(
                route = NoteDest.NoteDetailsDest.route + "?noteId={noteId}"
            ) {
                NoteDetailsScreen(
                    showSnackbar = showSnackbar,
                    onPopBackStack = { navController.popBackStack() }
                )
            }
        })
}
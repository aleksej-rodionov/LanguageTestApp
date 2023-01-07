package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.languagetestapp.feature_notes.presentation.note_details.NoteDetailsScreen
import com.example.languagetestapp.feature_notes.presentation.note_list.NoteListScreen
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest
import com.example.languagetestapp.feature_profile.presentation.camera.CameraScreen
import com.example.languagetestapp.feature_profile.presentation.change_password.ChangePasswordScreen
import com.example.languagetestapp.feature_profile.presentation.pick_image.PickImageScreen
import com.example.languagetestapp.feature_profile.presentation.profile.ProfileScreen
import com.example.languagetestapp.feature_profile.presentation.util.ProfileDest

@Composable
fun NoteNavigation(
    navController: NavHostController,
    //this Composable need to pass the showSnackbar function on to the screens
    showSnackbar: (String, SnackbarDuration) -> Unit,
    openDrawerClick: () -> Unit,
    logout: () -> Unit
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
                    },
                    openDrawerClick = {
                        openDrawerClick()
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

            composable(
                route = ProfileDest.ProfileDestination.route
            ) {
                ProfileScreen(
                    showSnackbar = showSnackbar,
                    popBackStack = { navController.popBackStack() },
                    logout = { logout() },
                    onNavigate = {
                        navController.navigate(it.route)
                    }
                )
            }

            composable(
                route = ProfileDest.ChangePasswordDest.route
            ) {
                ChangePasswordScreen(
                    showSnackbar = showSnackbar,
                    onPopBackStack = { navController.popBackStack() }
                )
            }

            composable(
                route = ProfileDest.CameraDest.route
            ) {
                CameraScreen(
                    showSnackbar = showSnackbar,
                    onPopBackStack = { navController.popBackStack() }
                )
            }

            composable(
                route = ProfileDest.PickImageDest.route
            ) {
                PickImageScreen(
                    showSnackbar = showSnackbar,
                    onPopBackStack = { navController.popBackStack() }
                )
            }
        })
}
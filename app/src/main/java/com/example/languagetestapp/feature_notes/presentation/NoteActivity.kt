package com.example.languagetestapp.feature_notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.languagetestapp.feature_notes.presentation.note_details.NoteDetailsScreen
import com.example.languagetestapp.feature_notes.presentation.note_list.NoteListScreen
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NoteDest.NoteListDest.route,
                    builder = {

                        composable(
                            route = NoteDest.NoteListDest.route
                        ) {
                            NoteListScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                }
                            )
                        }

                        composable(
                            route = NoteDest.NoteDetailsDest.route
                        ) {
                            NoteDetailsScreen()
                        }
                    })
            }
        }
    }
}
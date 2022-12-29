package com.example.languagetestapp.feature_notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.languagetestapp.feature_notes.presentation.note_details.NoteDetailsScreen
import com.example.languagetestapp.feature_notes.presentation.note_list.NoteListScreen
import com.example.languagetestapp.feature_notes.presentation.util.NoteDest
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val noteViewModel: NoteViewModel = hiltViewModel()
                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()

                LaunchedEffect(key1 = true) {
                    noteViewModel.uiEvent.collectLatest { event ->
                        when (event) {
                            is NoteUiEvent.SnackbarMsg -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.msg
                                )
                            }
                        }
                    }
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    content = { pv ->

                        Box(
                            modifier = Modifier.padding(pv)
                        ) {

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
                                        route = NoteDest.NoteDetailsDest.route + "?noteId={noteId}"
                                    ) {
                                        NoteDetailsScreen(
                                            onPopBackStack = { navController.popBackStack() },
                                            noteViewModel = noteViewModel
                                        )
                                    }
                                })
                        }
                    })
            }
        }
    }
}

@Composable
fun NoteNavGraph(
    viewModel: NoteViewModel
) {

}










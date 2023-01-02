package com.example.languagetestapp.feature_notes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.startActivity
import com.example.languagetestapp.feature_auth.presentation.AuthActivity
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val noteViewModel: NoteViewModel = hiltViewModel()
                NoteMainComposable(
                    toAuthActivity = { startActivity<AuthActivity>() },
                    noteViewModel = noteViewModel
                )
            }
        }
    }
}










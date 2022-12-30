package com.example.languagetestapp.feature_splash.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.startActivity
import com.example.languagetestapp.feature_auth.presentation.AuthActivity
import com.example.languagetestapp.feature_notes.presentation.NoteActivity
import com.example.languagetestapp.ui.theme.Blue
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import com.example.languagetestapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SplashCustomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val viewModel: SplashCustomViewModel = hiltViewModel()

                LaunchedEffect(key1 = true, block = {
                    viewModel.isLogged.collectLatest {
                        if (it) {
                            toNoteActivity()
                        } else {
                            toAuthActivity()
                        }
                    }
                })

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Blue
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "LanguageApiTestApp",
                            fontSize = 28.sp,
                            color = White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    private fun toNoteActivity() {
        startActivity<NoteActivity>()
    }

    private fun toAuthActivity() {
        startActivity<AuthActivity>()
    }
}
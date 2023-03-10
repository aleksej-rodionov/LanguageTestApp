package com.example.languagetestapp.feature_splash.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.feature_auth.presentation.AuthActivity
import com.example.languagetestapp.feature_notes.presentation.NoteActivity
import com.example.languagetestapp.ui.theme.Blue
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import com.example.languagetestapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LanguageSplashActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val viewModel: LanguageSplashViewModel = hiltViewModel()

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
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
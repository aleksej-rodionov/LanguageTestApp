package com.example.languagetestapp.feature_auth.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.languagetestapp.feature_auth.presentation.login.LoginScreen
import com.example.languagetestapp.feature_auth.presentation.register.RegisterScreen
import com.example.languagetestapp.feature_auth.presentation.util.AuthDest
import com.example.languagetestapp.feature_notes.presentation.NoteActivity
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AuthDest.LoginDest.route,
                    builder =  {

                        composable(
                            route = AuthDest.LoginDest.route
                        ) {
                            LoginScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                },
                                toNoteActivity = {
                                    toNoteActivity()
                                }
                            )
                        }

                        composable(
                            route = AuthDest.RegisterDest.route
                        ) {
                            RegisterScreen(
                                toNoteActivity = {
                                    toNoteActivity()
                                }
                            )
                        }

                    })
            }
        }
    }

    private fun toNoteActivity() {
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
        finish()
    }
}
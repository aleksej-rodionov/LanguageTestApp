package com.example.languagetestapp.feature_auth.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.languagetestapp.feature_auth.presentation.util.AuthDest
import com.example.languagetestapp.ui.theme.LanguageTestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageTestAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AuthDest.LoginDest.route,
                    builder =  {
                        composable(AuthDest.LoginDest.route) {

                        }
                        composable(AuthDest.RegisterDest.route) {

                        }
                    })
            }
        }
    }
}
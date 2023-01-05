package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ProfileBar(
    title: String,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Open drawer",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onLogoutClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        title = {
            Text(text = title)
        }
    )
}
package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        backgroundColor = Color.DarkGray,
        title = {
            Text(text = title, color = Color.White)
        },
        modifier = Modifier.background(Color.DarkGray),
        elevation = 0.dp
    )
}
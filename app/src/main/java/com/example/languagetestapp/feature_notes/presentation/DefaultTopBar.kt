package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DefaultTopBar(
    title: String,
    onBackClick: () -> Unit
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
        backgroundColor = MaterialTheme.colors.primary,
        title = {
            Text(text = title)
        }
    )
}
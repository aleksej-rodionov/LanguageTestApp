package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoteDrawerBody(
    email: String?,
    onLogoutCLick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = email ?: "Not logged in"
            )
            IconButton (
                onClick = {
                    onLogoutCLick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout"
                )
            }
        }
    }
}
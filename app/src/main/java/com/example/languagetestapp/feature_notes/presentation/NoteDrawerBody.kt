package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
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
    onLogoutCLick: () -> Unit, // todo remove
    onChangePasswordClick: () -> Unit,// todo remove then
    toProfileClick: () -> Unit
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
                text = email ?: "Not logged in",
                Modifier.clickable {
                    toProfileClick()
                }
            )
            IconButton(
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
        Spacer(modifier = Modifier.height(32.dp))
        Button( // todo remove
            onClick = { onChangePasswordClick() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Change password")
        }
    }
}
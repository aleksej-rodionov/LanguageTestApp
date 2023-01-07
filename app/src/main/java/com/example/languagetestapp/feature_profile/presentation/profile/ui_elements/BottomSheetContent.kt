package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomSheetContent(
    onCameraClick: () -> Unit,
    onPickFileClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            /*.height(300.dp)*/
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Change avatar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )
        Divider()
        ProfileItem(
            text = "Camera",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCameraClick()
                },
            imageVector = Icons.Default.Camera
        )
        Divider()
        ProfileItem(
            text = "Choose file",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onPickFileClick()
                },
            imageVector = Icons.Default.Folder
        )
    }
}
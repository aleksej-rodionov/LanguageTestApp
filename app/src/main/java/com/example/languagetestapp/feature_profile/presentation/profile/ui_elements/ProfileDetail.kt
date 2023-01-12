package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.rememberMotionLayoutState
import coil.compose.AsyncImage
import com.example.languagetestapp.R

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileDetail(
    email: String?,
    currentAvaUrl: String?,
    newAvaUrl: String?,
    avaUploadProgress: Int?,
    onChangePasswordClick: () -> Unit,
    onAvaClick: () -> Unit
) {

    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.profile_motion_scene)
            .readBytes()
            .decodeToString()
    }
    val motionState = rememberMotionLayoutState()

    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        motionLayoutState = motionState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .layoutId("box")
        )

        AsyncImage(
            model = currentAvaUrl ?: newAvaUrl,
            contentDescription = null,
            error = painterResource(R.drawable.ava_placeholder),
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .layoutId("profile_pic")
                .clickable {
                    onAvaClick()
                },
            contentScale = ContentScale.Crop
        )

        Text(
            text = ((email ?: " email not found ") + avaUploadProgress?.let {
                "${it}%"
            }),
            fontSize = 24.sp,
            modifier = Modifier.layoutId("email"),
            color = Color.White
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White)
                .layoutId("content_bg")
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .layoutId("text")
                .padding(horizontal = 16.dp)
        ) {

            ProfileItem(
                text = "Change password",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onChangePasswordClick()
                    },
                imageVector = Icons.Default.Lock
            )
            Divider()
        }
    }
}










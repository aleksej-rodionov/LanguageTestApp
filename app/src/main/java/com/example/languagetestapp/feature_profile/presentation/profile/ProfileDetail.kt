package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.rememberMotionLayoutState
import com.example.languagetestapp.R

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileDetail(
    state: ProfileState,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
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

        Image(
            painter = painterResource(id = R.drawable.ava_placeholder),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .layoutId("profile_pic")
        )

        Text(
            text = "test.email.cz",
            fontSize = 24.sp,
            modifier = Modifier.layoutId("email"),
            color = Color.White,
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White)
                .layoutId("content_bg")
        )

        Text(
            text = "Unto the angel of the church of Ephesus write; These things saith he that holdeth the seven stars in his right hand, who walketh in the midst of the seven golden candlesticks;\n" +
                    "I know thy works, and thy labour, and thy patience, and how thou canst not bear them which are evil: and thou hast tried them which say they are apostles, and are not, and hast found them liars:\n" +
                    "And hast borne, and hast patience, and for my name's sake hast laboured, and hast not fainted.\n" +
                    "Nevertheless I have somewhat against thee, because thou hast left thy first love.\n" +
                    "Remember therefore from whence thou art fallen, and repent, and do the first works; or else I will come unto thee quickly, and will remove thy candlestick out of his place, except thou repent.",
            modifier = Modifier
                .fillMaxHeight()
                .layoutId("text")
                .padding(horizontal = 16.dp),
            fontSize = 14.sp
        )

//        Button(
//            onClick = {
//
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
//            modifier = Modifier.layoutId("back"), contentPadding = PaddingValues(4.dp),
//            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
//        ) {
//            Icon(
//                Icons.Default.ArrowBack,
//                contentDescription = ""
//            )
//        }
//
//        Button(
//            onClick = {
//
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
//            modifier = Modifier.layoutId("logout"), contentPadding = PaddingValues(4.dp),
//            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
//        ) {
//            Icon(
//                Icons.Default.Logout,
//                contentDescription = ""
//            )
//        }
    }
}










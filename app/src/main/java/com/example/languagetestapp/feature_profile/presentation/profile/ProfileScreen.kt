package com.example.languagetestapp.feature_profile.presentation.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    popBackStack: () -> Unit,
    logout: () -> Unit,
    onNavigate: (ProfileUiEvent.Navigate) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.Navigate -> {
                    onNavigate(event)
                }
                is ProfileUiEvent.PopBackStack -> {
                    popBackStack()
                }
                is ProfileUiEvent.Logout -> {
                    logout()
                }
                is ProfileUiEvent.SnackbarMsg -> {
                    showSnackbar(event.msg, SnackbarDuration.Short)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ProfileBar(
                title = ""/*"Profile"*/,
                onBackClick = {
                    viewModel.onAction(ProfileAction.OnBackClick)
                },
                onLogoutClick = {
                    viewModel.onAction(ProfileAction.OnLogoutClick)
                }
            ) }
    ) { pv ->

        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
        )
        val scope = rememberCoroutineScope()
        BackHandler(sheetState.isVisible) {
            scope.launch { sheetState.hide() }
        }

        ModalBottomSheetLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv),
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
            sheetContent = {

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
                                viewModel.onAction(ProfileAction.OnCamera)
                            },
                        imageVector = Icons.Default.Camera
                    )
                    Divider()
                    ProfileItem(
                        text = "Choose file",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onAction(ProfileAction.OnPickFileImage)
                            },
                        imageVector = Icons.Default.Folder
                    )
                }
            },
        ) {

            ProfileDetail(
                state = state,
                onChangePasswordClick = {
                    viewModel.onAction(ProfileAction.OnChangePasswordClick)
                },
                onToggleBottomSheet = {
                    scope.launch {
                        if (sheetState.isVisible) {
                            sheetState.hide()
                        } else {
                            sheetState.show()
                        }
                    }
                }
            )
        }
    }
}






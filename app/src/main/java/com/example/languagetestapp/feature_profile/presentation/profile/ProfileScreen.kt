package com.example.languagetestapp.feature_profile.presentation.profile

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.core.util.permission.PermissionHandler
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
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

    val permissions = remember { //todo later separate permission to 2 on differet Bottomsheet click
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    val permissionHandler = remember(permissions) { PermissionHandler() }
    val permissionsStates by permissionHandler.state.collectAsState()
    HandlePermissionsRequest(permissions = permissions, permissionHandler = permissionHandler)

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
                onAvaClick = {
                    scope.launch {
                        //todo check permission if granted toggle bottomSheet

                    }
                }
            )
        }
    }



    //====================PRIVATE METHODS====================
    fun onAvatarClicked() {
        if (permissionsStates.multiplePermissionState?.allPermissionsGranted == true) {
            if (sheetState.isVisible) {
                sheetState.hide()
            } else {
                sheetState.show()
            }
        } else {
            Button (onClick = {
                permissionHandler.onAction(PermissionHandler.PermissionAction.PermissionRequired)
            }) {
                Text(text = "Request Permission")
            }
        }
    }
}






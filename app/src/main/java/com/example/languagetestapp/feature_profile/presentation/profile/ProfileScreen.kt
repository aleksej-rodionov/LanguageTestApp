package com.example.languagetestapp.feature_profile.presentation.profile

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.core.util.permission.HandlePermissionsRequest
import com.example.languagetestapp.core.util.permission.PermissionHandler
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

//====================PERMISSION====================
    val permissions = remember {
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    val permissionHandler = remember(permissions) { PermissionHandler() }
    val permissionsStates by permissionHandler.state.collectAsState()
    HandlePermissionsRequest(permissions = permissions, permissionHandler = permissionHandler)
//====================PERMISSION END====================

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

                BottomSheetContent(
                    onCameraClick = { viewModel.onAction(ProfileAction.OnCamera) },
                    onPickFileClick = { viewModel.onAction(ProfileAction.OnPickFileImage) }
                )
            },
        ) {

            ProfileDetail(
                onChangePasswordClick = {
                    viewModel.onAction(ProfileAction.OnChangePasswordClick)
                },
                onAvaClick = {
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






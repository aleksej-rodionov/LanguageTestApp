package com.example.languagetestapp.feature_profile.presentation.change_password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagetestapp.feature_notes.presentation.DefaultTopBar
import com.example.languagetestapp.feature_notes.presentation.note_details.NoteDetailsAction

@Composable
fun ChangePasswordScreen(
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val scaffolState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ChangePasswordUiEvent.SnackbarMsg -> {
                    showSnackbar(event.msg, SnackbarDuration.Short)
                }
                is ChangePasswordUiEvent.PopBackStack -> {
                    onPopBackStack()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffolState,
        topBar = {
            DefaultTopBar(
                title = "Note details",
                onBackClick = {
                    viewModel.onAction(ChangePasswordAction.Back)
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { pv ->

        Box(
            modifier = Modifier.padding(pv)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center
            ) {

                TextField(
                    value = state.oldPassword,
                    onValueChange = {
                        viewModel.onAction(ChangePasswordAction.OldPasswordChanged(it))
                    },
                    isError = state.oldPasswordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Old password")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.oldPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (state.oldPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val desc =
                            if (state.oldPasswordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {
                            viewModel.onAction(ChangePasswordAction.OldPasswordVisibilityChanged(!state.oldPasswordVisible))
                        }) {
                            Icon(imageVector = image, contentDescription = desc)
                        }
                    }
                )
                if (state.oldPasswordError != null) {
                    Text(
                        text = state.oldPasswordError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = state.newPassword,
                    onValueChange = {
                        viewModel.onAction(ChangePasswordAction.NewPasswordChanged(it))
                    },
                    isError = state.newPasswordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Old password")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.newPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (state.newPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val desc =
                            if (state.newPasswordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {
                            viewModel.onAction(ChangePasswordAction.NewPasswordVisibilityChanged(!state.newPasswordVisible))
                        }) {
                            Icon(imageVector = image, contentDescription = desc)
                        }
                    }
                )
                if (state.newPasswordError != null) {
                    Text(
                        text = state.newPasswordError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = state.repeatedNewPassword,
                    onValueChange = {
                        viewModel.onAction(ChangePasswordAction.RepeatedNewPasswordChanged(it))
                    },
                    isError = state.repeatedNewPasswordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Old password")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.repeatedNewPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (state.repeatedNewPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val desc =
                            if (state.repeatedNewPasswordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {
                            viewModel.onAction(ChangePasswordAction.RepeatedNewPasswordVisibilityChanged(!state.repeatedNewPasswordVisible))
                        }) {
                            Icon(imageVector = image, contentDescription = desc)
                        }
                    }
                )
                if (state.repeatedNewPasswordError != null) {
                    Text(
                        text = state.repeatedNewPasswordError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.onAction(ChangePasswordAction.Submit)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Submit")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}










package com.example.languagetestapp.feature_auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    toNoteActivity: () -> Unit, // todo fire it from somewhere
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RegisterUiEvent.SnackbarMsg -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.msg)
                }
                is RegisterUiEvent.ToNoteActivity -> {
                    toNoteActivity()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(color = Color.LightGray)
    ) { pv ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
        ) {
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = state.email,
                    onValueChange = {
                        viewModel.onAction(RegisterAction.EmailChanged(it))
                    },
                    isError = state.emailError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Email")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                if (state.emailError != null) {
                    Text(
                        text = state.emailError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = state.password,
                    onValueChange = {
                        viewModel.onAction(RegisterAction.PasswordChanged(it))
                    },
                    isError = state.passwordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Password")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (state.passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                        val desc = if (state.passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {
                            viewModel.onAction(RegisterAction.PasswordVisibilityChanged(!state.passwordVisible))
                        }) {
                            Icon(imageVector = image, contentDescription = desc)
                        }
                    }
                )
                if (state.passwordError != null) {
                    Text(
                        text = state.passwordError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = state.repeatedPassword,
                    onValueChange = {
                        viewModel.onAction(RegisterAction.RepeatedPasswordChanged(it))
                    },
                    isError = state.repeatedPasswordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Repeat password")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (state.repeatedPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (state.repeatedPasswordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                        val desc = if (state.repeatedPasswordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {
                            viewModel.onAction(RegisterAction.RepeatedPasswordVisibilityChanged(!state.repeatedPasswordVisible))
                        }) {
                            Icon(imageVector = image, contentDescription = desc)
                        }
                    }
                )
                if (state.repeatedPasswordError != null) {
                    Text(
                        text = state.repeatedPasswordError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.termsAccepted,
                        onCheckedChange = {
                            viewModel.onAction(RegisterAction.AcceptTerms(it))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Accept terms")
                }
                if (state.termsError != null) {
                    Text(
                        text = state.termsError,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                Button(
                    onClick = {
                        viewModel.onAction(RegisterAction.Submit)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Submit")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            
            if (state.successfullyRegistered) {
                Image(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
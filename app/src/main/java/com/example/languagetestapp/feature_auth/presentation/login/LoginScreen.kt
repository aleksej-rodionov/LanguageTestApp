package com.example.languagetestapp.feature_auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onNavigate: (LoginUiEvent.Navigate) -> Unit,
    toNoteActivity: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LoginUiEvent.Navigate -> {
                    onNavigate(event)
                }
                is LoginUiEvent.ToNoteActivity -> {
                    toNoteActivity()
                }
                is LoginUiEvent.SnackbarMsg -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.msg
                    )
                }
            }
        }
    })

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
                        viewModel.onAction(LoginAction.EmailChanged(it))
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
                        viewModel.onAction(LoginAction.PasswordChanged(it))
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
                            viewModel.onAction(LoginAction.PasswordVisibilityChanged(!state.passwordVisible))
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

                Text(
                    text = "Not registered yet?",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = AnnotatedString("SignUp"),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(true) {
                            viewModel.onAction(LoginAction.NotRegisteredYetClick)
                        },
                    color = Color.Blue
                )

                Button(
                    onClick = {
                        viewModel.onAction(LoginAction.Submit)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Login")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
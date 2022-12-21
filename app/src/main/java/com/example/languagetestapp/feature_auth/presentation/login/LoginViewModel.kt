package com.example.languagetestapp.feature_auth.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Constants.TAG_AUTH
import com.example.languagetestapp.feature_auth.domain.use_case.Login
import com.example.languagetestapp.feature_auth.domain.use_case.ValidateEmail
import com.example.languagetestapp.feature_auth.domain.use_case.ValidatePassword
import com.example.languagetestapp.feature_auth.presentation.util.AuthDest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val login: Login
): ViewModel() {

    var state by mutableStateOf(LoginState())

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> {
                state = state.copy(email = action.email)
            }
            is LoginAction.PasswordChanged -> {
                state = state.copy(password = action.password)
            }
            is LoginAction.NotRegisteredYetClick -> {
                goToRegisterScreen()
            }
            is LoginAction.Submit -> {
                login()
            }
        }
    }

    private fun goToRegisterScreen() = viewModelScope.launch {
        _uiEvent.send(LoginUiEvent.Navigate(AuthDest.RegisterDest.route))
    }

    private fun login() {
        Log.d(TAG_AUTH, "login: CALLED")
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password, true)
        val hasError = emailResult.errorMessage != null || passwordResult.errorMessage != null

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            Log.d(TAG_AUTH, "login: EXECUTE")
            val resp = login.execute(state.email, state.password)
            if (resp.data != null) {
                Log.d(TAG_AUTH, "login response: ${resp.data ?: "Xuj znajet kakoj kluc"}")
                // todo parse token and save accessToken and refreshToken to prefStore
                _uiEvent.send(LoginUiEvent.ToNoteActivity)

            } else {
                Log.d(TAG_AUTH, "login response: ${resp.message ?: "Xuj znajet"}")
                // todo show snackbar with the Error Message
            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null
)

sealed class LoginAction() {
    data class EmailChanged(val email: String): LoginAction()
    data class PasswordChanged(val password: String): LoginAction()
    object NotRegisteredYetClick: LoginAction()
    object Submit: LoginAction()
}

sealed class LoginUiEvent() {
    data class Navigate(val route: String): LoginUiEvent()
    object ToNoteActivity: LoginUiEvent()
}


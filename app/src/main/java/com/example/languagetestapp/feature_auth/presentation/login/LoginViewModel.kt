package com.example.languagetestapp.feature_auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            is LoginAction.PasswordVisibilityChanged -> {
                state = state.copy(passwordVisible = action.nowVisible)
            }
        }
    }

    private fun goToRegisterScreen() = viewModelScope.launch {
        _uiEvent.send(LoginUiEvent.Navigate(AuthDest.RegisterDest.route))
    }

    private fun login() {
//        Log.d(TAG_AUTH, "login: CALLED")
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
            state = state.copy(isLoading = true)
            val resp = login.execute(state.email, state.password)

            if (resp.data != null) {
                _uiEvent.send(LoginUiEvent.ToNoteActivity)
                state = state.copy(isLoading = false)
            } else {
                _uiEvent.send(LoginUiEvent.SnackbarMsg(resp.message ?: "No token data found in ViewModel layer"))
                state = state.copy(isLoading = false)
            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = false
)

sealed class LoginAction() {
    data class EmailChanged(val email: String): LoginAction()
    data class PasswordChanged(val password: String): LoginAction()
    object NotRegisteredYetClick: LoginAction()
    object Submit: LoginAction()
    data class PasswordVisibilityChanged(val nowVisible: Boolean): LoginAction()
}

sealed class LoginUiEvent() {
    data class Navigate(val route: String): LoginUiEvent()
    object ToNoteActivity: LoginUiEvent()
    data class SnackbarMsg(val msg: String): LoginUiEvent()
}


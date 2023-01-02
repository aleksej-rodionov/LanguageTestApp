package com.example.languagetestapp.feature_auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_auth.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val validateTerms: ValidateTerms,
    private val register: Register,
    private val login: Login
): ViewModel() {

    var state by mutableStateOf(RegisterState())

    private val _uiEvent = Channel<RegisterUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when(action) {
            is RegisterAction.EmailChanged -> {
                state = state.copy(email = action.email)
            }
            is RegisterAction.PasswordChanged -> {
                state = state.copy(password = action.password)
            }
            is RegisterAction.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = action.repeatedPassword)
            }
            is RegisterAction.AcceptTerms -> {
                state = state.copy(termsAccepted = action.accepted)
            }
            is RegisterAction.Submit -> {
                submitData()
            }
            is RegisterAction.PasswordVisibilityChanged -> {
                state = state.copy(passwordVisible = action.nowVisible)
            }
            is RegisterAction.RepeatedPasswordVisibilityChanged -> {
                state = state.copy(repeatedPasswordVisible = action.nowVisible)
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password, false)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.password, state.repeatedPassword
        )
        val termsResult = validateTerms.execute(state.termsAccepted)

        val hasError = listOf(
            emailResult, passwordResult, repeatedPasswordResult, termsResult
        ).any {
            it.errorMessage != null
        }

        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val resp = register.execute(state.email, state.password)
            resp.data?.let {
                state = state.copy(isLoading = false)
                state = state.copy(successfullyRegistered = true)
                loginWithCredentialsJustRegistered(it.email, it.password)
            } ?: run {
                _uiEvent.send(RegisterUiEvent.SnackbarMsg(resp.message ?: "No token data found in ViewModel layer"))
                state = state.copy(isLoading = false)
            }
        }
    }

    private fun loginWithCredentialsJustRegistered(email: String?, password: String?) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val resp = login.execute(state.email, state.password)

            if (resp.data != null) {
                _uiEvent.send(RegisterUiEvent.ToNoteActivity)
                state = state.copy(isLoading = false)
            } else {
                _uiEvent.send(RegisterUiEvent.SnackbarMsg(resp.message ?: "No token data found in ViewModel layer"))
                state = state.copy(isLoading = false)
            }
        }
    }
}

data class RegisterState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val termsAccepted: Boolean = false,
    val termsError: String? = null,
    val isLoading: Boolean = false,
    val successfullyRegistered: Boolean = false,
    val passwordVisible: Boolean = false,
    val repeatedPasswordVisible: Boolean = false
)

sealed class RegisterAction() {
    data class EmailChanged(val email: String): RegisterAction()
    data class PasswordChanged(val password: String): RegisterAction()
    data class RepeatedPasswordChanged(val repeatedPassword: String): RegisterAction()
    data class AcceptTerms(val accepted: Boolean): RegisterAction()
    data class PasswordVisibilityChanged(val nowVisible: Boolean): RegisterAction()
    data class RepeatedPasswordVisibilityChanged(val nowVisible: Boolean): RegisterAction()
    object Submit: RegisterAction()
}

sealed class RegisterUiEvent() {
    data class SnackbarMsg(val msg: String): RegisterUiEvent()
    object ToNoteActivity: RegisterUiEvent()
}
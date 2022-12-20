package com.example.languagetestapp.feature_auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_auth.domain.use_case.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {

    var registerFormState by mutableStateOf(RegisterFormState())

    private val _uiEvent = Channel<RegisterFormUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: RegisterFormAction) {
        when(action) {
            is RegisterFormAction.EmailChanged -> {
                registerFormState = registerFormState.copy(email = action.email)
            }
            is RegisterFormAction.PasswordChanged -> {
                registerFormState = registerFormState.copy(password = action.password)
            }
            is RegisterFormAction.RepeatedPasswordChanged -> {
                registerFormState = registerFormState.copy(repeatedPassword = action.repeatedPassword)
            }
            is RegisterFormAction.AcceptTerms -> {
                registerFormState = registerFormState.copy(termsAccepted = action.accepted)
            }
            is RegisterFormAction.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = authUseCases.validateEmail.execute(registerFormState.email)
        val passwordResult = authUseCases.validatePassword.execute(registerFormState.password)
        val repeatedPasswordResult = authUseCases.validateRepeatedPassword.execute(
            registerFormState.password, registerFormState.repeatedPassword
        )
        val termsResult = authUseCases.validateTerms.execute(registerFormState.termsAccepted)

        val hasError = listOf(
            emailResult, passwordResult, repeatedPasswordResult, termsResult
        ).any {
            it.errorMessage != null
        }

        if (hasError) {
            registerFormState = registerFormState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            authUseCases.register.execute(registerFormState.email, registerFormState.password)
            _uiEvent.send(RegisterFormUiEvent.ValidationSuccess)
        }
    }
}

data class RegisterFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val termsAccepted: Boolean = false,
    val termsError: String? = null
)

sealed class RegisterFormAction() {
    data class EmailChanged(val email: String): RegisterFormAction()
    data class PasswordChanged(val password: String): RegisterFormAction()
    data class RepeatedPasswordChanged(val repeatedPassword: String): RegisterFormAction()
    data class AcceptTerms(val accepted: Boolean): RegisterFormAction()

    object Submit: RegisterFormAction()
}

sealed class RegisterFormUiEvent() {
    object ValidationSuccess: RegisterFormUiEvent()
//    object PopBackStack: RegisterFormUiEvent()
//    data class Navigate(val route: String): RegisterFormUiEvent()
}
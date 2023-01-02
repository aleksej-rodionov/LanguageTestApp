package com.example.languagetestapp.feature_notes.presentation.change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.use_case.ChangePassword
import com.example.languagetestapp.feature_auth.domain.use_case.ValidatePassword
import com.example.languagetestapp.feature_auth.domain.use_case.ValidateRepeatedPassword
import com.example.languagetestapp.feature_auth.presentation.register.RegisterAction
import com.example.languagetestapp.feature_notes.presentation.note_details.NoteDetailsUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val changePassword: ChangePassword
): ViewModel() {

    var state by mutableStateOf(ChangePasswordState())

    private val _uiEvent = Channel<ChangePasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ChangePasswordAction) {
        when (action) {
            is ChangePasswordAction.OldPasswordChanged -> {
                state = state.copy(oldPassword = action.oldPassword)
            }
            is ChangePasswordAction.NewPasswordChanged -> {
                state = state.copy(newPassword = action.newPassword)
            }
            is ChangePasswordAction.RepeatedNewPasswordChanged -> {
                state = state.copy(repeatedNewPassword = action.repeatedNewPassword)
            }
            is ChangePasswordAction.OldPasswordVisibilityChanged -> {
                state = state.copy(oldPasswordVisible = action.nowVisible)
            }
            is ChangePasswordAction.NewPasswordVisibilityChanged -> {
                state = state.copy(newPasswordVisible = action.nowVisible)
            }
            is ChangePasswordAction.RepeatedNewPasswordVisibilityChanged -> {
                state = state.copy(repeatedNewPasswordVisible = action.nowVisible)
            }
            is ChangePasswordAction.Submit -> {
                submitData()
            }
        }
    }


    private fun submitData() {
        val oldPasswordResult = validatePassword.execute(state.oldPassword, true)
        val newPasswordResult = validatePassword.execute(state.newPassword, false)
        val repeatedNewPasswordResult = validateRepeatedPassword.execute(
            state.newPassword, state.repeatedNewPassword
        )
        val hasError = listOf(
            oldPasswordResult,
            newPasswordResult,
            repeatedNewPasswordResult
        ).any {
            it.errorMessage != null
        }

        if (hasError) {
            state = state.copy(
                oldPasswordError = oldPasswordResult.errorMessage,
                newPasswordError = newPasswordResult.errorMessage,
                repeatedNewPasswordError = repeatedNewPasswordResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val resp = changePassword.execute(
                state.oldPassword, state.newPassword
            )
            state = state.copy(isLoading = false)
            when (resp) {
                is Resource.Success -> {
                    _uiEvent.send(ChangePasswordUiEvent.SnackbarMsg(
                        "Password successfully changed"
                    ))
                    _uiEvent.send(ChangePasswordUiEvent.PopBackStack)
                }
                is Resource.Error -> {
                    _uiEvent.send(ChangePasswordUiEvent.SnackbarMsg(
                        resp.message ?: "Unknown error in VM layer"
                    ))
                }
            }
        }
    }
}


data class ChangePasswordState(
    val oldPassword: String = "",
    val oldPasswordError: String? = null,
    val newPassword: String = "",
    val newPasswordError: String? = null,
    val repeatedNewPassword: String = "",
    val repeatedNewPasswordError: String? = null,
    val oldPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val repeatedNewPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val successfullyChangedPassword: Boolean = false
)

sealed class ChangePasswordAction() {
    data class OldPasswordChanged(val oldPassword: String): ChangePasswordAction()
    data class NewPasswordChanged(val newPassword: String): ChangePasswordAction()
    data class RepeatedNewPasswordChanged(val repeatedNewPassword: String): ChangePasswordAction()
    data class OldPasswordVisibilityChanged(val nowVisible: Boolean): ChangePasswordAction()
    data class NewPasswordVisibilityChanged(val nowVisible: Boolean): ChangePasswordAction()
    data class RepeatedNewPasswordVisibilityChanged(val nowVisible: Boolean): ChangePasswordAction()
    object Submit: ChangePasswordAction()
}

sealed class ChangePasswordUiEvent() {
    data class SnackbarMsg(val msg: String): ChangePasswordUiEvent()
    object PopBackStack: ChangePasswordUiEvent()
}
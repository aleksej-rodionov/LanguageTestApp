package com.example.languagetestapp.feature_notes.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.model.User
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_notes.domain.repo.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val authRepo: AuthRepo
): ViewModel() {

    var state by mutableStateOf(NoteActivityState())

    private val _uiEvent = Channel<NoteActivityUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: NoteActivityAction) = viewModelScope.launch {
        when (action) {
            is NoteActivityAction.SearchWidgetStateChanged -> {
                state = state.copy(searchWidgetState = action.state)
            }
            is NoteActivityAction.SearchTextChanged -> {
                state = state.copy(searchText = action.text)
            }
            is NoteActivityAction.OnLogout -> {
                logout()
            }
        }
    }

    init {
        fetchCurrentUserData()?.email?.let {
            state = state.copy(userEmail = it)
        }
    }



    //====================PRIVATE METHODS====================
    private fun fetchCurrentUserData(): User? {
        return noteRepo.fetchCurrentUserData()
    }

    private fun logout() = viewModelScope.launch {
        val result = authRepo.logout()
        when (result) {
            is Resource.Success -> {
                _uiEvent.send(NoteActivityUiEvent.ToAuthActivity)
            }
            is Resource.Error -> {
                NoteActivityUiEvent.SnackbarMsg(result.message ?: "Unknown error")
            }
        }
    }
}

data class NoteActivityState(
    val searchWidgetState: SearchWidgetState = SearchWidgetState.Closed,
    val searchText: String = "",
    val userEmail: String = "email not found"
)

sealed class NoteActivityAction {
    data class SearchWidgetStateChanged(val state: SearchWidgetState): NoteActivityAction()
    data class SearchTextChanged(val text: String): NoteActivityAction()
    object OnLogout: NoteActivityAction()
}

sealed class NoteActivityUiEvent {
    data class SnackbarMsg(val msg: String): NoteActivityUiEvent()
    object ToAuthActivity: NoteActivityUiEvent()
}
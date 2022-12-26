package com.example.languagetestapp.feature_splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashCustomViewModel @Inject constructor(
    val authRepo: AuthRepo
): ViewModel() {

    private val _isLogged = MutableSharedFlow<Boolean>()
    val isLogged: SharedFlow<Boolean> = _isLogged.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(1000L)

            val accessToken = authRepo.fetchAccessToken()
            val logged = accessToken != null

            _isLogged.emit(logged)
        }
    }
}
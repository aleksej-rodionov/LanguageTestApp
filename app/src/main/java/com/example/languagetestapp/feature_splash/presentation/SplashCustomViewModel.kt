package com.example.languagetestapp.feature_splash.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_auth.util.Constants.TAG_USER
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
            val res = authRepo.getCurrentUserInfo()
            when (res) {
                is Resource.Success -> {
                    val accessToken = authRepo.fetchAccessToken()
                    val logged = accessToken != null
                    _isLogged.emit(logged)
                }
                is Resource.Error -> {
                    _isLogged.emit(false)
                }
            }
        }
    }
}
package com.example.languagetestapp.feature_splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageSplashViewModel @Inject constructor(

): ViewModel() {

    private val _isLogged = MutableSharedFlow<Boolean>()
    val isLogged: SharedFlow<Boolean> = _isLogged.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(1000L)
            val checkIfLogged = false
            _isLogged.emit(checkIfLogged)
        }
    }
}
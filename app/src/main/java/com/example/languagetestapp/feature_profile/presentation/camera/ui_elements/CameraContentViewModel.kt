package com.example.languagetestapp.feature_profile.presentation.camera.ui_elements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraContentViewModel @Inject constructor(
    private val fileRepo: FileRepo
): ViewModel() {

    var state by mutableStateOf(State())




    //====================STATE AND EVENT====================
    data class State(
        val imageUri: String? = null,
        val hasImage: Boolean = false
    )
}

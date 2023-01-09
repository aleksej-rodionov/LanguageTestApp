package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.util.Constants.TAG_FILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickImageContentViewModel @Inject constructor(
    private val fileRepo: FileRepo
): ViewModel() {

    val state = mutableStateOf(State())

    fun onImageSelected(uri: Uri) = viewModelScope.launch {

        // copy external file to app-specific storage
        val internalUriResult = fileRepo.copyFileFromExternal(uri)

        internalUriResult.data?.let {

            Log.d(TAG_FILE, "Internal File = $it")
            // upload
            val result = fileRepo.startUploadingImage(it)
            result.data?.let {
                Log.d(TAG_FILE, "Upload Result = ${it}")
            } ?: run {
                Log.d(TAG_FILE, "Upload Result = ${result.message ?: "Unknown error"}")
            }
        }
    }




    //====================STATE AND EVENT====================
    data class State(
        val uploadPercentage: Int? = null
    )

    sealed class Event { //todo use later instead direct callbacks
        data class UploadImage(val uri: Uri): Event()
        object LaunchImagePicker: Event()
    }

    sealed class UiEffect {

    }
}
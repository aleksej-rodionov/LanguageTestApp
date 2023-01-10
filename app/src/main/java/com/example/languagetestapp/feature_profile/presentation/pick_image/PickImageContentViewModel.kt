package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PickImageContentViewModel @Inject constructor(
    private val fileRepo: FileRepo,
    private val fileStatefulRepo: FileStatefulRepo
): ViewModel() {

    var state by mutableStateOf(State())

    init {
        viewModelScope.launch {
            fileStatefulRepo.progressUpdated.collectLatest {
                state = state.copy(uploadPercentage = it)
            }
        }
    }



    fun onImageSelected(uri: Uri) = viewModelScope.launch {
        val internalUriResult = fileRepo.copyFileFromExternal(uri) // copy external file to app-specific storage
        internalUriResult.data?.let { executeUploadingBytes(it) } // prepare multipart body
    }

    private fun executeUploadingBytes(imageFile: File) = viewModelScope.launch(Dispatchers.IO) {
        val resource = fileRepo.prepareFileFromInternalStorage(imageFile)
        when (resource) {
            is Resource.Error -> {
                //todo showSnackbar
            }
            is Resource.Success -> { resource.data?.let { part -> uploadFinalBodyPart(part) } }
        }
    }

    private fun uploadFinalBodyPart(part: MultipartBody.Part) = viewModelScope.launch(Dispatchers.IO) {
        val result = fileRepo.postFile(part)
        when (result) {
            is Resource.Success -> {
//                state = state.copy(remoteImageUrl = result.data ?: "Success but no data")
                state = state.copy(remoteImageUrl = "http://i1.wallbox.ru/wallpapers/main2/202028/15944759085f09c584aadc87.43708441.jpg") // for testing
            }
            is Resource.Error -> {
                state = state.copy(remoteImageUrl = "Show snackbar = " + result.message ?: "Show snackbar")
            }
        }
    }




    //====================STATE AND EVENT====================
    data class State(
        val uploadPercentage: Int? = null,
        val remoteImageUrl: String? = null,
        val hasImage: Boolean = false
    )

    sealed class Event { //todo use later instead direct callbacks
        data class UploadImage(val uri: Uri): Event()
        object LaunchImagePicker: Event()
    }

    sealed class UiEffect {
        data class SnackbarMsg(val msg: String): UiEffect()
    }
}
package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import com.example.languagetestapp.feature_file.domain.repo.ProgressResource
import com.example.languagetestapp.feature_file.util.Constants.TAG_FILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        // copy external file to app-specific storage
        val internalUriResult = fileRepo.copyFileFromExternal(uri)

        internalUriResult.data?.let {

            Log.d(TAG_FILE, "Internal File = $it")
            // upload
            executeUploadingBytes(it)
        }
    }

    var bytesUploadingJob: Job? = null
    private fun executeUploadingBytes(imageFile: File) {
        bytesUploadingJob?.cancel()
        bytesUploadingJob = viewModelScope.launch(Dispatchers.IO) {
            fileRepo.executeUploadingBytes(imageFile).onEach { progressResource ->
                when (progressResource) {
                    is ProgressResource.Progress -> {
                        Log.d(TAG_FILE, "executeUploadingBytes: Progress = ${progressResource.percentage ?: "NULL"}")
                    }
                    is ProgressResource.Error -> {
                        Log.d(TAG_FILE, "executeUploadingBytes: Error = ${progressResource.message ?: "Unknown error"}")
                    }
                    is ProgressResource.Success -> {
                        Log.d(TAG_FILE, "executeUploadingBytes: Success = ${progressResource.data ?: "Success but no data"}")
                        progressResource.data?.let { part ->
                            uploadFinalBodyPart(part)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun uploadFinalBodyPart(part: MultipartBody.Part) = viewModelScope.launch(Dispatchers.IO) {
        val result = fileRepo.postFile(part)
        when (result) {
            is Resource.Success -> {
                Log.d(TAG_FILE, "uploadFinalBodyPart: Success = ${result.data ?: "NULL"}")
                state = state.copy(remoteUrl = result.data ?: "Success but no data")
            }
            is Resource.Error -> {
                Log.d(TAG_FILE, "uploadFinalBodyPart: Error = ${result.message ?: "Unknown error"}")
                state = state.copy(remoteUrl = "Show snackbar = " + result.message ?: "Show snackbar")
            }
        }
    }




    //====================STATE AND EVENT====================
    data class State(
        val uploadPercentage: Int? = null,
        val remoteUrl: String? = null
    )

    sealed class Event { //todo use later instead direct callbacks
        data class UploadImage(val uri: Uri): Event()
        object LaunchImagePicker: Event()
    }

    sealed class UiEffect {

    }
}
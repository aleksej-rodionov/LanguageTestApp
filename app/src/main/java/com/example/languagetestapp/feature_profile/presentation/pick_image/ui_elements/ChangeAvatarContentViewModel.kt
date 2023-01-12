package com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import com.example.languagetestapp.feature_file.util.CustomFileProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

const val TAG_COMPARE_URI = "TAG_COMPARE_URI"

@HiltViewModel
class ChangeAvatarContentViewModel @Inject constructor(
    private val fileRepo: FileRepo,
    private val fileStatefulRepo: FileStatefulRepo,
    private val authRepo: AuthRepo // todo must be replaced by new userRepo for such methods
) : ViewModel() {

    var state by mutableStateOf(State())

    private val _uiEffect = Channel<UiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            fileStatefulRepo.progressUpdated.collectLatest {
                state = state.copy(uploadPercentage = it)
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.LaunchImagePicker -> {
                viewModelScope.launch { _uiEffect.send(UiEffect.LaunchPicker) }
            }
            is Event.LaunchCamera -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val uri = fileRepo.provideUriForCamera()
                    withContext(Dispatchers.Main) {
                        state = state.copy(hasImage = false) //todo remove?
                        state = state.copy(localImageUri = uri.toString())
                        _uiEffect.send(UiEffect.LaunchCameraWithUri(uri))
                    }
                }
            }
            is Event.OnImageSelected -> {
                state = state.copy(hasImage = true)
                onImageSelected(event.uri)
            }
            is Event.OnCameraSuccess -> {
                state = state.copy(hasImage = true)
                executeUploadFromLocalStorageToBodyPart()
            }
            is Event.Submit -> {
                updateUserWithNewAva()
            }
        }
    }


    private fun onImageSelected(uri: Uri) = viewModelScope.launch {

        val internalUriResult =
            fileRepo.copyFileFromExternal(uri) // copy external file to app-specific storage

        internalUriResult.data?.let {
            val localUri = it.toUri().toString()
            Log.d(TAG_COMPARE_URI, "onImageSelected: localUri = $localUri")
            state = state.copy(localImageUri = localUri)
            executeUploadFromLocalStorageToBodyPart(/*it*/) // prepare multipart body
        }
    }

    fun executeUploadFromLocalStorageToBodyPart(/*imageFile: File*/) =
        viewModelScope.launch(Dispatchers.IO) {
            state.localImageUri?.toUri()?.let { uri ->
                Log.d(TAG_COMPARE_URI, "executeUploadingBytes: localUri = $uri")
                val imageFile = File(uri.path)
                val resource = fileRepo.prepareFileFromInternalStorage(imageFile)
                when (resource) {
                    is Resource.Error -> {
                        //todo showSnackbar
                    }
                    is Resource.Success -> {
                        resource.data?.let { part -> sendFinalBodyPart(part) }
                    }
                }
            }
        }

    private fun sendFinalBodyPart(part: MultipartBody.Part) =
        viewModelScope.launch(Dispatchers.IO) {
            val result = fileRepo.postFile(part)
            when (result) {
                is Resource.Success -> {
                    state =
                        state.copy(
                            remoteImageUrl = "http://i1.wallbox.ru/wallpapers/main2/202028/15944759085f09c584aadc87.43708441.jpg"
                        ) // for testing
                }
                is Resource.Error -> {
                    state = state.copy(
                        remoteImageUrl = "Show snackbar = " + result.message ?: "Show snackbar"
                    )
                }
            }
        }

    fun updateUserWithNewAva() = viewModelScope.launch {
        state.remoteImageUrl?.let {
            authRepo.updateAva(it) // for final submit and save new ava to user in BackEnd
        }
    }


    //====================STATE AND EVENT====================
    data class State(
        val uploadPercentage: Int? = null,
        val localImageUri: String? = null,
        val remoteImageUrl: String? = null, // todo make old and new?
        val hasImage: Boolean = false
    )

    sealed class Event { //todo use later instead direct callbacks
        object LaunchImagePicker : Event()
        object LaunchCamera : Event()
        data class OnImageSelected(val uri: Uri) : Event()
        object OnCameraSuccess : Event()
        object Submit: Event()
    }

    sealed class UiEffect {
        data class SnackbarMsg(val msg: String) : UiEffect()
        object LaunchPicker : UiEffect()
        data class LaunchCameraWithUri(val uri: Uri) : UiEffect()
    }
}
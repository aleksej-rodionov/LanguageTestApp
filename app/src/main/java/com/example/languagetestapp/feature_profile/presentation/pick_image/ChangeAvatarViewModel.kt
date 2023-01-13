package com.example.languagetestapp.feature_profile.presentation.pick_image

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.core.util.permission.PermissionHandler
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import com.example.languagetestapp.feature_file.util.Constants.TAG_PROGRESS
import com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements.ChangeAvatarContentViewModel
import com.example.languagetestapp.feature_profile.presentation.pick_image.ui_elements.TAG_COMPARE_URI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(
    val permissionsHandler: PermissionHandler,
    savedStateHandle: SavedStateHandle,

    private val fileRepo: FileRepo,
    private val fileStatefulRepo: FileStatefulRepo,
    private val authRepo: AuthRepo // todo must be replaced by new userRepo for such methods
) : ViewModel() {

    private var _imageSource: String? = IMAGE_SOURCE_FILEPICKER

    var state by mutableStateOf(State())

    private val _uiEffect = Channel<UiEffect>()
    val uiEvent = _uiEffect.receiveAsFlow()

    init {
        _imageSource = savedStateHandle.get<String>("source")
        state = state.copy(
            imageSourceChosen = _imageSource ?: IMAGE_SOURCE_FILEPICKER
        )

        permissionsHandler.state.onEach { permHandlerState ->
            state = state.copy(
                multiplePermissionsState = permHandlerState.multiplePermissionState
            )
        }.catch {
            _uiEffect.send(
                UiEffect.SnackbarMsg(it.message ?: "Error fetching PermHandler State"))
        }.launchIn(viewModelScope)

        // observe progress in stateful repo
        viewModelScope.launch {
            fileStatefulRepo.progressUpdated.collectLatest {
                Log.d(TAG_PROGRESS, "ViewModel. Observer: $it %")
                state = state.copy(uploadPercentage = it)
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.PermDenied -> {
                viewModelScope.launch { _uiEffect.send(UiEffect.SnackbarMsg("PickFile perm Denied nax!!!!!!!00")) }
                viewModelScope.launch { _uiEffect.send(UiEffect.PopBackStack) }
            }
            is Event.PermissionRequired -> {
                onPermissionRequired()
            }

            is Event.LaunchImagePicker -> {
                viewModelScope.launch { _uiEffect.send(UiEffect.LaunchPicker) }
            }
            is Event.LaunchCamera -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val uri = fileRepo.provideUriForCamera()
                    withContext(Dispatchers.Main) {
                        state = state.copy(hasImage = false)
                        state = state.copy(localImageUri = uri.toString())
//                        state = state.copy(localImageUri =
//                        "file:///data/user/0/com.example.languagetestapp.fileprovider/my_images/selected_image_7633194652895633746.jpg"
//                        )
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
            is Event.LaunchSourceHandled -> {
                state = state.copy(launchSourceHandled = true)
            }
            is Event.Submit -> {
                updateUserWithNewAva()
            }
        }
    }


    //====================PRIVATE METHODS====================
    private fun onPermissionRequired() {
        permissionsHandler.onEvent(PermissionHandler.Event.PermissionRequired)
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

    private fun executeUploadFromLocalStorageToBodyPart(/*imageFile: File*/) =
        viewModelScope.launch(Dispatchers.IO) {
            state.localImageUri?.toUri()?.let { uri ->
//                Log.d(TAG_COMPARE_URI, "executeUploadingBytes: localUri = $uri")
                val imageFile = File(uri.path)
                Log.d(TAG_COMPARE_URI, "executeUploadingBytes: localUri as File = $uri")
                val resource = fileRepo.prepareFileFromInternalStorage(imageFile)
                when (resource) {
                    is Resource.Error -> {
                        _uiEffect.send(UiEffect.SnackbarMsg("Loading BodyPart:\n" + resource.message ?: "Unknown error loading bodyPart"))
                    }
                    is Resource.Success -> {
                        resource.data?.let { part -> sendFinalBodyPart(part) }
                    }
                }
            }
        }

    private fun sendFinalBodyPart(part: MultipartBody.Part) =
        viewModelScope.launch(Dispatchers.IO) {
//            Log.d(TAG_COMPARE_URI, "sendFinalBodyPart: part = ${part.body}")
            val result = fileRepo.postFile(part)
            when (result) {
                is Resource.Success -> {
                    state = state.copy(
                            remoteImageUrl = "http://i1.wallbox.ru/wallpapers/main2/202028/15944759085f09c584aadc87.43708441.jpg"
                        ) // for testing
                }
                is Resource.Error -> {
                    _uiEffect.send(UiEffect.SnackbarMsg("Sending File:\n" + result.message ?: "Unknown error sending file"))
                }
            }
        }

    private fun updateUserWithNewAva() = viewModelScope.launch {
        Log.d(TAG_COMPARE_URI, "updateUserWithNewAva: remoteUriToSetIntoUser = ${state.remoteImageUrl ?: "NULL"}")
        state.remoteImageUrl?.let {
            val result = authRepo.updateAva(it) // for final submit and save new ava to user in BackEnd
            when (result) {
                is Resource.Success -> {
                    _uiEffect.send(UiEffect.SnackbarMsg("Successfully changed ava"))
                }
                is Resource.Error -> {
                    _uiEffect.send(UiEffect.SnackbarMsg(result.message ?: "Unknown error submitting ava"))
                }
            }
        }
    }


    //====================STATE AND EVENT====================
    data class State @OptIn(ExperimentalPermissionsApi::class) constructor(
        val imageSourceChosen: String = IMAGE_SOURCE_FILEPICKER,
        val permissionRequestInFlight: Boolean = false,
        val hasCameraPermission: Boolean = false,
        val multiplePermissionsState: MultiplePermissionsState? = null,
        val permissionAction: PermissionHandler.Action = PermissionHandler.Action.NO_ACTION,

        val uploadPercentage: Int? = null,
        val localImageUri: String? = null,
        val remoteImageUrl: String? = null,
        val hasImage: Boolean = false,
        val launchSourceHandled: Boolean = false
    )

    sealed class Event {
        object PermDenied : Event()
        object PermissionRequired : Event()

        object LaunchImagePicker : Event()
        object LaunchCamera : Event()
        data class OnImageSelected(val uri: Uri) : Event()
        object OnCameraSuccess : Event()
        object LaunchSourceHandled: Event()
        object Submit: Event()
    }

    sealed class UiEffect {
        data class SnackbarMsg(val msg: String) : UiEffect()
        object PopBackStack : UiEffect()

//        data class SnackbarMsg(val msg: String) : ChangeAvatarContentViewModel.UiEffect()
        object LaunchPicker : UiEffect()
        data class LaunchCameraWithUri(val uri: Uri) : UiEffect()
    }
}
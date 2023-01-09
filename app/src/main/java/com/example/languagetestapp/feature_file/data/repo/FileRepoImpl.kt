package com.example.languagetestapp.feature_file.data.repo

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import com.example.languagetestapp.core.di.ApplicationScope
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_file.data.remote.FileApi
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import com.example.languagetestapp.feature_file.domain.repo.ProgressResource
import com.example.languagetestapp.feature_file.util.Constants.TAG_FILE
import com.example.languagetestapp.feature_file.util.getFileName
import com.example.languagetestapp.feature_profile.presentation.util.FileManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileRepoImpl(
    private val fileApi: FileApi,
    val context: Context,
    val fileStatefulRepo: FileStatefulRepo
) : FileRepo/*, UploadRequestBody.UploadProgressCallback */ {

    override fun prepareFileFromInternalStorage(fileUri: Uri?): MultipartBody.Part? {
        TODO("Not yet implemented")
    }

    override suspend fun postFile(requestBodyFile: MultipartBody.Part): Resource<String> {
        try {
            val response = fileApi.postFile(requestBodyFile)
            if (response.status == "ok") {
                response.body?.let { url ->
                    return Resource.Success(url)
                } ?: run {
                    return Resource.Error("Success, but user data not found")
                }
            } else {
                return Resource.Error(response.error ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Unknown exception")
        }
    }

    override fun copyFileFromExternal(externalUri: Uri): Resource<File> {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            externalUri, "r", null
        ) ?: run {
            Log.d(TAG_FILE, "ParcelFileDescriptor is NULL")
            return Resource.Error("ParcelFileDescriptor is NULL")
        }

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val newFile = File(
            context.cacheDir,
            context.contentResolver.getFileName(externalUri)
        )
        val outputStream = FileOutputStream(newFile)

        //todo do I need to launch this below in Coroutine and wait for completion?
        inputStream.copyTo(outputStream)
        Log.d(TAG_FILE, "copyFileFromExternal: Success. File = $newFile")
        return Resource.Success(newFile)
    }

    override fun executeUploadingBytes(imageFile: File):
            Flow<ProgressResource<MultipartBody.Part>> = flow {

        var loadingPercentage = 0
        emit(ProgressResource.Progress(loadingPercentage))

        val body = UploadRequestBody(
            file = imageFile,
            contentType = "image",
            onProgressChanged = {
//                emit(ProgressResource.Progress<MultipartBody.Part>(it))
                fileStatefulRepo.onProgressUpdated(it)
            }
        )

        val part = MultipartBody.Part.createFormData("image", imageFile.name, body)
        val result = ProgressResource.Success(part)
        emit(result)
    }



    //====================PRIVATE METHODS====================
    fun File.getMimeType(fallback: String = "image/*"): String {
        return MimeTypeMap.getFileExtensionFromUrl(toString())
            ?.run {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(toLowerCase())
            }
            ?: fallback
    }
}
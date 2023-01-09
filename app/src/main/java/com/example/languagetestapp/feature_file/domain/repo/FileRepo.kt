package com.example.languagetestapp.feature_file.domain.repo

import android.net.Uri
import com.example.languagetestapp.core.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File

interface FileRepo {

    suspend fun postFile(requestBodyFile: MultipartBody.Part): Resource<String>

    fun copyFileFromExternal(externalUri: Uri): Resource<File>

    suspend fun prepareFileFromInternalStorage(imageFile: File): Resource<MultipartBody.Part>
}




//todo move somewhere
sealed class ProgressResource<T>(
    val data: T? = null,
    val message: String? = null,
    val percentage: Int? = null
) {
    class Success<T>(data: T?): ProgressResource<T>(data)
    class Error<T>(message: String, data: T? = null): ProgressResource<T>(data, message)
    class Progress<T>(percentage: Int?): ProgressResource<T>(percentage = percentage)
}
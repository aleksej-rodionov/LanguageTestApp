package com.example.languagetestapp.feature_file.data.repo

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.example.languagetestapp.core.util.Resource
import com.example.languagetestapp.feature_file.data.remote.FileApi
import com.example.languagetestapp.feature_file.domain.repo.FileRepo
import com.example.languagetestapp.feature_file.util.getFileName
import com.example.languagetestapp.feature_profile.presentation.util.FileManager
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileRepository(
    private val fileApi: FileApi,
    val context: Context
) : FileRepo {

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
        ) ?: return Resource.Error("ParcelFileDescriptor is NULL")

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val newFile = File(
            context.cacheDir,
            context.contentResolver.getFileName(externalUri)
        )
        val outputStream = FileOutputStream(newFile)
        inputStream.copyTo(outputStream)
        return Resource.Success(newFile)
    }

    //====================PRIVATE METHODS====================

}
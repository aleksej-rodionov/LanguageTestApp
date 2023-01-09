package com.example.languagetestapp.feature_file.domain.repo

import android.net.Uri
import com.example.languagetestapp.core.util.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File

interface FileRepo {

    fun prepareFileFromInternalStorage(fileUri: Uri?): MultipartBody.Part?

    suspend fun postFile(requestBodyFile: MultipartBody.Part): Resource<String>

    fun copyFileFromExternal(externalUri: Uri): Resource<File>
}
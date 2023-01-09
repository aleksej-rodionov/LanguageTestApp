package com.example.languagetestapp.feature_file.data.remote

import com.example.languagetestapp.feature_file.data.remote.model.FileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {

    @Multipart
    @POST("/upload")
    suspend fun postFile(
        @Part file: MultipartBody.Part
    ): FileResponse<String>
}
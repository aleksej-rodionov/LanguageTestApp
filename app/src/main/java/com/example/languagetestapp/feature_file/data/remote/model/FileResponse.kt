package com.example.languagetestapp.feature_file.data.remote.model

import com.google.gson.annotations.SerializedName

data class FileResponse<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("body")
    val body: T?,
    @SerializedName("error")
    val error: String?
)


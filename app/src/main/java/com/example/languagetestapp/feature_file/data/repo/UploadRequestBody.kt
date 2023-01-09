package com.example.languagetestapp.feature_file.data.repo

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.languagetestapp.feature_file.util.Constants.TAG_FILE
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file: File,
    private val contentType: String,
    private val callback: UploadProgressCallback //todo replace with lambda
): RequestBody() {

    override fun contentType(): MediaType? = "$contentType/*".toMediaTypeOrNull()

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val totalLength = file.length()
        Log.d(TAG_FILE, "writeTo: file.length() = $totalLength")
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploadedLength = 0L

        fileInputStream.use {
            var step = 0
            val handler = Handler(Looper.getMainLooper()) //todo replace with flow

            while (fileInputStream.read(buffer).also { step = it } != -1) {
                handler.post(ProgressUpdate(uploadedLength, totalLength))
                uploadedLength += step
                sink.write(buffer, 0, step)
            }
        }
    }




    //todo replace with plain flow then
    inner class ProgressUpdate(
        private val uploadedLength: Long,
        private val totalLength: Long
    ): Runnable {

        override fun run() {
            callback.onProgressChanged((100 * uploadedLength / totalLength).toInt())
        }
    }

    interface UploadProgressCallback {
        fun onProgressChanged(percentage: Int)
    }

    companion object {
        const val DEFAULT_DUFFER_SIZE = 1024
    }
}
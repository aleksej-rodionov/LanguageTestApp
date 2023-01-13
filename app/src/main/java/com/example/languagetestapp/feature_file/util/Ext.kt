package com.example.languagetestapp.feature_file.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri


fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

//fun Uri.refactorIfStartsWithContent(): Uri {
//    return if (this.toString().startsWith("content://")) {
//        val properStarting = "file:///data/user/0/"
//        val rightPart = this.toString().split("//")[1]
//        val newUri = (properStarting + rightPart).toUri()
//        return newUri
//    } else {
//        return this
//    }
//}
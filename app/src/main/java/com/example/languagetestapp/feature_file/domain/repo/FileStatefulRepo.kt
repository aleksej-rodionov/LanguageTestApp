package com.example.languagetestapp.feature_file.domain.repo

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface FileStatefulRepo {

    val progressUpdated: StateFlow<Int?>

    fun onProgressUpdated(progress: Int?)
}
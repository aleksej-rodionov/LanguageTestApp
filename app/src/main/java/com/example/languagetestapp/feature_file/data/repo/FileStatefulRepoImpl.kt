package com.example.languagetestapp.feature_file.data.repo

import com.example.languagetestapp.feature_file.domain.repo.FileStatefulRepo
import com.example.languagetestapp.feature_notes.domain.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FileStatefulRepoImpl(
    private val fileScope: CoroutineScope
): FileStatefulRepo {

    private val _progressUpdated = MutableStateFlow<Int?>(null)
    override val progressUpdated: StateFlow<Int?> = _progressUpdated.asStateFlow()

    override fun onProgressUpdated(progress: Int?) {
        _progressUpdated.value = progress
    }
}
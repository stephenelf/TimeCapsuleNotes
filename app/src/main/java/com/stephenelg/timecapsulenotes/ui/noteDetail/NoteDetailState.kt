package com.stephenelg.timecapsulenotes.ui.noteDetail

import okhttp3.internal.concurrent.Task

data class NoteDetailState(
    val id: String? = null,
    val title: String? = "",
    val description: String? = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isNoteDeleted: Boolean = false
)

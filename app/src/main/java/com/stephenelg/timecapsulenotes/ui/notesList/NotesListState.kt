package com.stephenelg.timecapsulenotes.ui.notesList

import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote

data class NotesListState(val notes: List<TimeCapsuleNote> = emptyList(), val isLoading: Boolean = false, val userMessage: Int? = null)

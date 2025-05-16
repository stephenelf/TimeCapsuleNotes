package com.stephenelg.timecapsulenotes.ui.noteDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    val noteId: String = savedStateHandle[TodoDestinationsArgs.NOTE_ID_ARG]!!

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isNoteDeleted = MutableStateFlow(false)

    val uiState: StateFlow<NoteDetailState> = combine(
        _userMessage, _isLoading, _isNoteDeleted, notesRepository.getNoteFlow(noteId)
    ) { userMessage, isLoading, isNoteDeleted, note ->
                NoteDetailState(
                    id = note?.id,
                    title = note?.title,
                    description = note?.content,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    isNoteDeleted = isNoteDeleted
                )
            }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteDetailState(isLoading = true)
        )

    fun deleteNote() = viewModelScope.launch {
     //   notesRepository.delete(taskId)
        _isNoteDeleted.value = true
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun refresh(){

    }
}
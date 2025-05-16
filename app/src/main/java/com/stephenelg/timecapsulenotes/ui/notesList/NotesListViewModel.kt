package com.stephenelg.timecapsulenotes.ui.notesList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelg.timecapsulenotes.R
import com.stephenelg.timecapsulenotes.domain.UIResources
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import com.stephenelg.timecapsulenotes.ui.ADD_EDIT_RESULT_OK
import com.stephenelg.timecapsulenotes.ui.DELETE_RESULT_OK
import com.stephenelg.timecapsulenotes.ui.EDIT_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    init {
        Log.d("ViewModel", "ViewModel init")
        // getAllNotes()
    }

   // private val _uiState = MutableStateFlow(NotesState(emptyList()))
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)


    val uiState: StateFlow<NotesListState> =
        combine(
            _isLoading,
            _userMessage,
            notesRepository.getAllNotes()
        ) {  loading, message, allNotes ->
            NotesListState(notes=allNotes, userMessage = message, isLoading = loading)

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NotesListState(emptyList(), isLoading = true)
        )

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            notesRepository.getAllNotes()
            _isLoading.value = false
        }
    }

    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ViewModel", "Getting all notes")
            notesRepository.getAll().collectLatest { resource ->
                when (resource) {
                    is UIResources.Success -> {
                       // _uiState.update {
                      //      it.copy(notes = resource.data)
                      //  }
                        // _uiState.value = NotesState(resource.data)
                        Log.d("ViewModel", "Success: ${resource.data}")
                    }

                    is UIResources.Error -> {
                        Log.d("ViewModel", "Error: ${resource.message}")
                    }

                    UIResources.Loading -> {
                        Log.d("ViewModel", "Loading...")
                    }
                }
            }
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_note_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_note_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_note_message)
        }
    }
}
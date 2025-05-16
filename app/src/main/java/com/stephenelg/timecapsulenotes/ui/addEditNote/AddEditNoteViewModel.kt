package com.stephenelg.timecapsulenotes.ui.addEditNote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stephenelg.timecapsulenotes.R
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import com.stephenelg.timecapsulenotes.domain.repository.PokemonRepository
import com.stephenelg.timecapsulenotes.ui.TodoDestinationsArgs
import com.stephenelg.timecapsulenotes.ui.notesList.NotesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val pokemonRepository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: String? = savedStateHandle[TodoDestinationsArgs.NOTE_ID_ARG]
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(AddEditNoteState())
    //  val uiState: StateFlow<AddEditNoteState> = _uiState.asStateFlow()

    val uiState: StateFlow<AddEditNoteState> =
        combine(
            _isLoading,
            _userMessage,
            pokemonRepository.getPokemonHabitats()
        ) { loading, message, habitats ->
            AddEditNoteState(habitats = habitats, userMessage = message, isLoading = loading)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AddEditNoteState(habitats = emptyList(), isLoading = true)
        )

    fun saveTask() {
        if (uiState.value.title.isEmpty() || uiState.value.content.isEmpty()) {
            //_uiState.update {
            //   it.copy(userMessage = R.string.empty_task_message)
            //}
            return
        }

        if (noteId == null) {
            createNewTask()
        } else {
            updateTask()
        }
    }

    private fun createNewTask() = viewModelScope.launch {
        notesRepository.createNote(uiState.value.title, uiState.value.content)
        _uiState.update {
            it.copy(isSaved = true)
        }
    }

    private fun updateTask() {
        if (noteId == null) {
            throw RuntimeException("updateTask() was called but task is new.")
        }
        viewModelScope.launch {
            notesRepository.updateTask(
                noteId,
                title = uiState.value.title,
                description = uiState.value.content,
            )
            _uiState.update {
                it.copy(isSaved = true)
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateContent(newContent: String) {
        _uiState.update {
            it.copy(content = newContent)
        }
    }

    fun updateHabitat(newContent: String) {
        _uiState.update {
            it.copy(habitat = newContent)
        }
    }

    fun updateDate(newContent: Date) {
        _uiState.update {
            it.copy(date = newContent)
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }
}
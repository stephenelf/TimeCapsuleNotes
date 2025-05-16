package com.stephenelg.timecapsulenotes.domain.repository

import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote
import com.stephenelg.timecapsulenotes.domain.UIResources
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun getAll(): Flow<UIResources<List<TimeCapsuleNote>>>

    suspend fun insertAll(vararg timeCapsuleNote: TimeCapsuleNote)

    suspend fun delete(timeCapsuleNote: TimeCapsuleNote)
    suspend fun createNote(title: String, content: String)
    suspend fun getNote(noteId: String): TimeCapsuleNote?
    suspend fun updateTask(noteId: String, title: String, description: String)

    fun getAllNotes(): Flow<List<TimeCapsuleNote>>
    fun getNoteFlow(noteId: String): Flow<TimeCapsuleNote?>
}
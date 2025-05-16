package com.stephenelg.timecapsulenotes.data.repository

import android.util.Log
import com.stephenelg.timecapsulenotes.data.TimeCapsuleNoteDao
import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote
import com.stephenelg.timecapsulenotes.domain.UIResources
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(private val timeCapsuleNoteDao: TimeCapsuleNoteDao) :
    NotesRepository {


    override fun getAllNotes(): Flow<List<TimeCapsuleNote>> {
        return timeCapsuleNoteDao.getAll()
    }

    override fun getNoteFlow(noteId: String): Flow<TimeCapsuleNote?> {
        return timeCapsuleNoteDao.getByIdFlow(noteId)
    }


    override suspend fun getAll(): Flow<UIResources<List<TimeCapsuleNote>>> =
        flow {
            Log.d("NotesRepositoryImpl", "getAll:")
            emit(UIResources.Loading)
            timeCapsuleNoteDao.getAll().collect { notes -> emit(UIResources.Success(notes)) }
        }.catch { e ->
            emit(UIResources.Error(e.localizedMessage ?: "Unknown error occurred"))
        }


    override suspend fun insertAll(vararg timeCapsuleNote: TimeCapsuleNote) {
        timeCapsuleNoteDao.insertAll(*timeCapsuleNote)
    }

    override suspend fun delete(timeCapsuleNote: TimeCapsuleNote) {
        TODO("Not yet implemented")
    }

    override suspend fun createNote(title: String, content: String) {
        // ID creation might be a complex operation so it's executed using the supplied
        // coroutine dispatcher
        val noteId = withContext(Dispatchers.IO) {
            UUID.randomUUID().toString()
        }
        val note = TimeCapsuleNote(id = noteId, title = title, content = content)
        timeCapsuleNoteDao.upsert(note)
    }

    override suspend fun getNote(noteId: String): TimeCapsuleNote? =
        timeCapsuleNoteDao.getById(noteId)

    override suspend fun updateTask(noteId: String, title: String, description: String) {
        val note = getNote(noteId)?.copy(
            title = title,
            content = description
        ) ?: throw Exception("Task (id $noteId) not found")
        timeCapsuleNoteDao.upsert(note)
    }

}
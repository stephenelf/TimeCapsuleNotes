package com.stephenelg.timecapsulenotes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeCapsuleNoteDao {

    @Query("SELECT * FROM timecapsulenote")
    fun getAll(): Flow<List<TimeCapsuleNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg timeCapsuleNote: TimeCapsuleNote)

    @Delete
    fun delete(timeCapsuleNote: TimeCapsuleNote)

    @Query("SELECT * FROM timecapsulenote WHERE id = :noteId")
    suspend fun getById(noteId: String): TimeCapsuleNote?


    /**
     * Insert or update a task in the database. If a task already exists, replace it.
     *
     * @param task the task to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(note: TimeCapsuleNote)

    @Query("DELETE FROM timecapsulenote")
    suspend fun clearAllTables()

    @Query("SELECT * FROM timecapsulenote WHERE id = :noteId")
    fun getByIdFlow(noteId: String): Flow<TimeCapsuleNote?>

}
package com.stephenelg.timecapsulenotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.stephenelg.timecapsulenotes.data.TimeCapsuleNotesDatabase
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import com.stephenelg.timecapsulenotes.data.repository.NotesRepositoryImpl
import org.junit.Before
import org.junit.Rule

class DatabaseRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var NotesRepository: NotesRepository

    @Before
    fun setUp() {
        val timeCapsuleNotesDatabase: TimeCapsuleNotesDatabase = Room.databaseBuilder(
            instantTaskExecut,
            TimeCapsuleNotesDatabase::class.java, "TimeCapsuleNotes"
        ).build()
        NotesRepository = NotesRepositoryImpl()

    }
}
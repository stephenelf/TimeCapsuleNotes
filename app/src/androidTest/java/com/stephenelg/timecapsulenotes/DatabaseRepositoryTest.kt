package com.stephenelg.timecapsulenotes

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote
import com.stephenelg.timecapsulenotes.data.TimeCapsuleNotesDatabase
import com.stephenelg.timecapsulenotes.domain.repository.NotesRepository
import com.stephenelg.timecapsulenotes.data.repository.NotesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseRepositoryTest {
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val timeCapsuleNotesDatabase: TimeCapsuleNotesDatabase = Room.databaseBuilder(
            context ,
            TimeCapsuleNotesDatabase::class.java, "TimeCapsuleNotes"
        ).build()

         /*
        val timeCapsuleNotesDatabase: TimeCapsuleNotesDatabase = Room.inMemoryDatabaseBuilder(
           context, TimeCapsuleNotesDatabase::class.java).build()
*/
        notesRepository = NotesRepositoryImpl(timeCapsuleNotesDatabase.timeCapsuleNoteDao())

    }



    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.stephenelg.timecapsulenotes", appContext.packageName)
    }

    @Test
    fun testGetAllNotes() = runTest{
        GlobalScope.launch(Dispatchers.IO) {
            notesRepository.getAll().collect{
                System.out.println(it)
            }
        }
    }

    @Test
    fun testInsertNote()= runTest {
        val note: TimeCapsuleNote = TimeCapsuleNote(1,"Test Title: ${System.currentTimeMillis()}","Test Content: ${System.currentTimeMillis()}")
        GlobalScope.launch(Dispatchers.IO) {
            notesRepository.insertAll(note)
        }
    }
}
package com.stephenelg.timecapsulenotes.domain

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.stephenelg.timecapsulenotes.data.TimeCapsuleNoteDao
import com.stephenelg.timecapsulenotes.data.TimeCapsuleNotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): TimeCapsuleNotesDatabase {
        Log.d("DatabaseModule", "Database created")
        return Room.databaseBuilder(
            applicationContext,
            TimeCapsuleNotesDatabase::class.java, "TimeCapsuleNotes"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: TimeCapsuleNotesDatabase): TimeCapsuleNoteDao {
        return db.timeCapsuleNoteDao()
    }
}
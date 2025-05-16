package com.stephenelg.timecapsulenotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stephenelg.timecapsulenotes.data.model.TimeCapsuleNote

@Database (entities = [TimeCapsuleNote :: class], version = 1)
@TypeConverters(Converters::class)
abstract class TimeCapsuleNotesDatabase : RoomDatabase() {
    abstract fun timeCapsuleNoteDao() : TimeCapsuleNoteDao

}
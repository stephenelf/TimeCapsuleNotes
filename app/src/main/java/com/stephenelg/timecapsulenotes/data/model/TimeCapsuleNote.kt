package com.stephenelg.timecapsulenotes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity (tableName = "timecapsulenote")
data class TimeCapsuleNote(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "date") val date: Date?= null
)

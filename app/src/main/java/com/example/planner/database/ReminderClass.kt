package com.example.planner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_table")
data class ReminderClass(
    @ColumnInfo(name = "reminder_text")
    var reminderText: String,
    @ColumnInfo(name = "deadline_date")
    var Rdeadline: String,
    @ColumnInfo(name = "priority")
    var Rpriority: Int ,
    @ColumnInfo(name = "flag")
    var RpriorityFlag: Int = 0
){
    @PrimaryKey(autoGenerate = true)
    var reminderID: Int = 0
}
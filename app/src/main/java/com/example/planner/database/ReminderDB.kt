package com.example.planner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReminderClass::class], version = 1, exportSchema = false)
abstract class ReminderDatabase : RoomDatabase() {

    abstract val reminderDatabasedao: ReminderDatabaseDAO

    companion object {

        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getInstance(context: Context): ReminderDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ReminderDatabase::class.java, "Reminders_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

annotation class ReminderDB

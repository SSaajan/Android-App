package com.example.planner.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReminderDatabaseDAO {
    @Insert
    fun insert(reminder: ReminderClass)

    @Update
    fun update(reminder: ReminderClass)

    @Query("SELECT * from reminder_table WHERE reminderID = :key")
    fun get(key: String): ReminderClass?

    @Query("DELETE FROM reminder_table")
    fun clear()

    @Query("DELETE FROM reminder_table WHERE reminder_text = :key")
    fun delete(key: String)

    @Query("SELECT * FROM reminder_table WHERE reminder_text = :key")
    fun getReminder(key: String): ReminderClass

    @Query("SELECT * FROM reminder_table ORDER BY priority DESC")
    fun getAllReminders(): LiveData<List<ReminderClass>>

    @Query("UPDATE reminder_table SET deadline_date = :key2, priority = :key3, flag = :key4 WHERE reminder_text = :key5")
    fun update(key2: String, key3: Int, key4: Int, key5: String)
}
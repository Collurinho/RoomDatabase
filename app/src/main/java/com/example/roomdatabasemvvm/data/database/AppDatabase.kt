package com.example.roomdatabasemvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomdatabasemvvm.data.model.Task
import com.example.roomdatabasemvvm.ui.task.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
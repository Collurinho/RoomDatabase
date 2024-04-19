package com.example.roomdatabasemvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.roomdatabasemvvm.data.model.Task
import com.example.roomdatabasemvvm.ui.task.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun insertTask(task: Task) = taskDao.insertTask(task)

    fun deleteTask(task: Task) = taskDao.deleteTask(task)
}
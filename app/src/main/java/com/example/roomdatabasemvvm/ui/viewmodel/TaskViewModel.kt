package com.example.roomdatabasemvvm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.roomdatabasemvvm.data.model.Task
import com.example.roomdatabasemvvm.data.repository.TaskRepository

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: LiveData<List<Task>> = repository.getAllTasks()

    fun insertTask(task: Task) = repository.insertTask(task)

    fun deleteTask(task: Task) = repository.deleteTask(task)
}
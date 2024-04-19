package com.example.roomdatabasemvvm.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.roomdatabasemvvm.R
import com.example.roomdatabasemvvm.data.database.AppDatabase
import com.example.roomdatabasemvvm.data.model.Task
import com.example.roomdatabasemvvm.data.repository.TaskRepository
import com.example.roomdatabasemvvm.databinding.ActivityMainBinding
import com.example.roomdatabasemvvm.ui.task.adapter.TaskAdapter
import com.example.roomdatabasemvvm.ui.viewmodel.TaskViewModel
import com.example.roomdatabasemvvm.ui.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TaskAdapter.OnDeleteItemClickListener {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "app-database").build()
        val taskDao = database.taskDao()
        val repository = TaskRepository(taskDao)
        val taskViewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, taskViewModelFactory).get(TaskViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter()
        taskAdapter.onDeleteItemClickListener = this
        recyclerView.adapter = taskAdapter

        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
        }

        binding.buttonAddTask.setOnClickListener {
            addTask()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addTask() {
        val taskTitle = binding.editTextTitle.text.toString().trim()
        val taskDescription = binding.editTextDescription.text.toString().trim()

        if (taskTitle.isNotEmpty() && taskDescription.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                val newTask = Task(title = taskTitle, description = taskDescription)
                taskViewModel.insertTask(newTask)
            }

            binding.editTextTitle.text.clear()
            binding.editTextDescription.text.clear()
        } else {
            when {
                taskTitle.isEmpty() && taskDescription.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.empty_field_title_and_description), Toast.LENGTH_SHORT).show()
                }
                taskTitle.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.empty_field_title), Toast.LENGTH_SHORT).show()
                }
                taskDescription.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.empty_field_description), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDeleteItemClick(task: Task) {
        // Starts a coroutine to perform task deletion on a separate thread
        GlobalScope.launch(Dispatchers.IO) {
            // Call method on ViewModel to delete the task
            taskViewModel.deleteTask(task)
        }
    }
}
package com.example.roomdatabasemvvm.ui.task.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasemvvm.R
import com.example.roomdatabasemvvm.data.model.Task

class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    var onDeleteItemClickListener: OnDeleteItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)
        private val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description

            deleteIcon.setOnClickListener {
                onDeleteItemClickListener?.onDeleteItemClick(task)
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    interface OnDeleteItemClickListener {
        fun onDeleteItemClick(task: Task)
    }
}
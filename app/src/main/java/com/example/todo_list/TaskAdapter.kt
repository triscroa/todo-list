package com.example.todo_list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskChecked: (Task) -> Unit,
    private val onTaskClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbCompleted: CheckBox = itemView.findViewById(R.id.cbTaskCompleted)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        val tvPriority: TextView = itemView.findViewById(R.id.tvPriorityBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvTitle.text = task.title
        holder.tvDueDate.text = "Due: ${task.dueDate}"
        holder.tvPriority.text = task.priority

        holder.cbCompleted.setOnCheckedChangeListener(null)
        holder.cbCompleted.isChecked = task.isCompleted
        toggleStrikeThrough(holder.tvTitle, task.isCompleted)

        holder.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            toggleStrikeThrough(holder.tvTitle, isChecked)
            onTaskChecked(task)
        }

        holder.itemView.setOnClickListener {
            onTaskClicked(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun getTaskAt(position: Int): Task = tasks[position]

    fun removeTaskAt(position: Int) {
        if (position in tasks.indices) {
            tasks.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(tvTitle: TextView, isCompleted: Boolean) {
        if (isCompleted) {
            tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
package com.example.todo_list

data class Task(
    val id: Int,
    val title: String,
    val dueDate: String,
    val priority: String, // e.g., "High", "Medium", "Low"
    var isCompleted: Boolean = false
)
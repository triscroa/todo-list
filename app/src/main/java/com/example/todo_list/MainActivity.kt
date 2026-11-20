package com.example.todo_list

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmptyState: TextView
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTasks = findViewById(R.id.rvTasks)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        val fabAddTask: FloatingActionButton = findViewById(R.id.fabAddTask)

        setupRecyclerView()
        loadSampleData()

        fabAddTask.setOnClickListener {
            // Hook this up to your teammate's Add Task dialog/screen
            Toast.makeText(this, "Open Add Task Screen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            tasks = taskList,
            onTaskChecked = { task ->
                // Notify database teammate or update state here
                updateEmptyState()
            },
            onTaskClicked = { task ->
                // Open edit/detail view
                Toast.makeText(this, "Clicked: ${task.title}", Toast.LENGTH_SHORT).show()
            }
        )
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = taskAdapter
    }

    private fun loadSampleData() {
        taskList.clear()
        taskList.add(Task(1, "Design Main UI", "Nov 22, 2026", "High", false))
        taskList.add(Task(2, "Connect Room Database", "Nov 24, 2026", "Medium", false))
        taskList.add(Task(3, "Test Swipe-to-Delete", "Nov 25, 2026", "Low", true))
        taskAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (taskList.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }
}
package com.example.todo_list

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private enum class FilterMode { ALL, ACTIVE, COMPLETED }
    private enum class SortMode { DEFAULT, PRIORITY_HIGH_FIRST, DUE_DATE_ASC, COMPLETION }

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmptyState: TextView

    // Master source list; displayed list is filtered/sorted dynamically from this
    private val allTasks = mutableListOf<Task>()
    private var currentFilter = FilterMode.ALL
    private var currentSort = SortMode.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTasks = findViewById(R.id.rvTasks)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        val fabAddTask: FloatingActionButton = findViewById(R.id.fabAddTask)
        val chipGroupFilter: ChipGroup = findViewById(R.id.chipGroupFilter)
        val btnSortTasks: MaterialButton = findViewById(R.id.btnSortTasks)

        setupRecyclerView()
        setupSwipeToDelete()
        setupFiltersAndSorting(chipGroupFilter, btnSortTasks)

        loadSampleData()
        refreshDisplayedTasks()

        fabAddTask.setOnClickListener {
            openAddTaskHook()
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            tasks = mutableListOf(),
            onTaskChecked = { task ->
                onTaskCompletionToggled(task)
            },
            onTaskClicked = { task ->
                openEditTaskHook(task)
            }
        )
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = taskAdapter
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val removedTask = taskAdapter.getTaskAt(position)
                taskAdapter.removeTaskAt(position)
                allTasks.removeAll { it.id == removedTask.id }
                updateEmptyState(taskAdapter.itemCount == 0)

                // Hook: notify database/repository teammate of deletion
                onTaskDeleted(removedTask)

                Snackbar.make(rvTasks, "Deleted \"${removedTask.title}\"", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        allTasks.add(removedTask)
                        onTaskCreated(removedTask)
                        refreshDisplayedTasks()
                    }
                    .show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(rvTasks)
    }

    private fun setupFiltersAndSorting(chipGroupFilter: ChipGroup, btnSortTasks: MaterialButton) {
        chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            currentFilter = when (checkedIds.firstOrNull()) {
                R.id.chipActive -> FilterMode.ACTIVE
                R.id.chipCompleted -> FilterMode.COMPLETED
                else -> FilterMode.ALL
            }
            refreshDisplayedTasks()
        }

        btnSortTasks.setOnClickListener {
            val options = arrayOf(
                "Default Order",
                "Priority (High to Low)",
                "Due Date (A–Z)",
                "Incomplete First"
            )
            AlertDialog.Builder(this)
                .setTitle("Sort Tasks By")
                .setItems(options) { _, which ->
                    currentSort = when (which) {
                        1 -> SortMode.PRIORITY_HIGH_FIRST
                        2 -> SortMode.DUE_DATE_ASC
                        3 -> SortMode.COMPLETION
                        else -> SortMode.DEFAULT
                    }
                    refreshDisplayedTasks()
                }
                .show()
        }
    }

    fun refreshDisplayedTasks() {
        val filtered = when (currentFilter) {
            FilterMode.ALL -> allTasks.toList()
            FilterMode.ACTIVE -> allTasks.filter { !it.isCompleted }
            FilterMode.COMPLETED -> allTasks.filter { it.isCompleted }
        }

        val sorted = when (currentSort) {
            SortMode.DEFAULT -> filtered
            SortMode.PRIORITY_HIGH_FIRST -> filtered.sortedBy { priorityRank(it.priority) }
            SortMode.DUE_DATE_ASC -> filtered.sortedBy { it.dueDate.lowercase() }
            SortMode.COMPLETION -> filtered.sortedBy { it.isCompleted }
        }

        taskAdapter.updateTasks(sorted)
        updateEmptyState(sorted.isEmpty())
    }

    private fun priorityRank(priority: String): Int = when (priority.trim().lowercase()) {
        "high" -> 0
        "medium" -> 1
        "low" -> 2
        else -> 3
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            tvEmptyState.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }

    private fun loadSampleData() {
        allTasks.clear()
        allTasks.add(Task(1, "Design Main UI", "Nov 22, 2026", "High", false))
        allTasks.add(Task(2, "Connect Local Database", "Nov 24, 2026", "Medium", false))
        allTasks.add(Task(3, "Test Swipe-to-Delete", "Nov 25, 2026", "Low", true))
    }

    // --- Group CRUD Integration Hooks ---

    // Call this when your teammate's Add Task screen/dialog returns a new Task
    fun onTaskCreated(newTask: Task) {
        allTasks.removeAll { it.id == newTask.id }
        allTasks.add(newTask)
        refreshDisplayedTasks()
        // TODO: Insert newTask into group database/repository
    }

    // Call this when your teammate's Edit Task screen/dialog returns an updated Task
    fun onTaskUpdated(updatedTask: Task) {
        val index = allTasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            allTasks[index] = updatedTask
        } else {
            allTasks.add(updatedTask)
        }
        refreshDisplayedTasks()
        // TODO: Update updatedTask in group database/repository
    }

    // Called automatically on swipe-to-delete (and Undo re-adds it)
    fun onTaskDeleted(deletedTask: Task) {
        // TODO: Delete deletedTask from group database/repository
    }

    private fun onTaskCompletionToggled(task: Task) {
        val index = allTasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            allTasks[index].isCompleted = task.isCompleted
        }
        if (currentFilter != FilterMode.ALL) {
            refreshDisplayedTasks()
        }
        // TODO: Persist task.isCompleted to group database/repository
    }

    private fun openAddTaskHook() {
        // TODO: Launch Add Task Activity/DialogFragment and pass result to onTaskCreated(task)
        Toast.makeText(this, "Hook: Open Add Task UI", Toast.LENGTH_SHORT).show()
    }

    private fun openEditTaskHook(task: Task) {
        // TODO: Launch Edit Task Activity/DialogFragment with task and pass result to onTaskUpdated(task)
        Toast.makeText(this, "Hook: Edit \"${task.title}\"", Toast.LENGTH_SHORT).show()
    }
}
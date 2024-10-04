package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.dao.TaskDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.ScrumboardConstants
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task

class TaskViewModel (private val taskDao: TaskDao): ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks
    fun getTasks() = viewModelScope.launch {
        taskDao.getAllTasks().catch { Log.e("TASK_VIEW_MODEL", it.toString()) }
            .collect { _tasks.emit(it) }
    }

    fun createTask(title: String,
                    description: String,
                    complexity: ScrumboardConstants.Complexity,
                   priority: ScrumboardConstants.Priority,
                   estimate: Int
                   ) = viewModelScope.launch {
        val task = Task(
            title = title,
            description = description,
            complexity = complexity,
            priority = priority,
            estimate = estimate,
            status = ScrumboardConstants.Status.TO_DO,
            assignedTo = null,
            storyId = 1,
            reviewerId = null
        )
        try {
            val taskId = taskDao.insertTask(task)
            Log.d("STORY_VIEW_MODEL", "Story has been created with id: $taskId")
        } catch (e: Exception) {
            Log.e("STORY_VIEW_MODEL", "Could not insert Story", e)
        }
    }
}
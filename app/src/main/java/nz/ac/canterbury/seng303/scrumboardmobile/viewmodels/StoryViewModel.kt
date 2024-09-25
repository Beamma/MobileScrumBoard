package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.datastore.Storage
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story
import nz.ac.canterbury.seng303.scrumboardmobile.models.Task
import kotlin.random.Random

class StoryViewModel(private val storyStorage: Storage<Story>): ViewModel() {
    private val _storyList = MutableStateFlow<List<Story>>(emptyList())
    val storyList: StateFlow<List<Story>> get() = _storyList

    private val _selectedStory = MutableStateFlow<Story?>(null)
    val selectedStory: StateFlow<Story?> get() = _selectedStory

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> get() = _selectedTask

    private val _username = MutableStateFlow("Anonymous")
    val username: StateFlow<String> get() = _username

    fun getStoryById(storyId: Int?) = viewModelScope.launch {
        if (storyId != null) {
            _selectedStory.value = storyStorage.get {it.getIdentifier() == storyId}.first()
        } else {
            _selectedStory.value = null
        }
    }

    fun getStories() = viewModelScope.launch {
        storyStorage.getAll().catch {
            Log.e("STORY_VIEW_MODEL", it.toString())
        }.collect {_storyList.emit(it)}
    }

    fun editUsername(username: String) = viewModelScope.launch {
        _username.value = username
    }

    fun createStory(title: String,
                    description: String,
                    tasks: List<Task>) = viewModelScope.launch {
        val story = Story(
            id = Random.nextInt(0, Int.MAX_VALUE),
            title = title,
            description = description,
            timestamp = System.currentTimeMillis(),
            taskList = tasks
        )
        storyStorage.insert(story).catch { Log.e("STORY_VIEW_MODEL", "Could not insert story") }
            .collect()
        storyStorage.getAll().catch { Log.e("STORY_VIEW_MODEL", it.toString()) }
            .collect{_storyList.emit(it)}
    }

    fun editStoryById(storyId: Int?, story: Story) = viewModelScope.launch {
        Log.d("STORY_VIEW_MODEL", "Editing story: $storyId")
        if (storyId != null) {
            storyStorage.edit(storyId, story).collect()
            storyStorage.getAll().catch { Log.e("STORY_VIEW_MODEL", it.toString()) }
                .collect{_storyList.emit(it)}
        }
    }
}
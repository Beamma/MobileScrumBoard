package nz.ac.canterbury.seng303.scrumboardmobile.viewmodels.story

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng303.scrumboardmobile.dao.StoryDao
import nz.ac.canterbury.seng303.scrumboardmobile.models.Story

class StoryViewModel (private val storyDao: StoryDao): ViewModel() {
    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> get() = _stories
    fun getStories() = viewModelScope.launch {
        storyDao.getAllStories().catch { Log.e("STORY_VIEW_MODEL", it.toString()) }
            .collect { _stories.emit(it) }
    }

    fun createStory(title: String,
                    description: String,
                    timeCreated: Long) = viewModelScope.launch {
        val story = Story(
            title = title,
            description = description,
            timeCreated = timeCreated
        )
        try {
            val storyId = storyDao.insertStory(story)
            Log.d("STORY_VIEW_MODEL", "Story has been created with id: $storyId")
        } catch (e: Exception) {
            Log.e("STORY_VIEW_MODEL", "Could not insert Story", e)
        }
    }
}


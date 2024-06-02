package com.example.storyapp_androidintermediate_sub1.view.add

import androidx.lifecycle.ViewModel
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addNewStory(file: File, description: String) = repository.addNewStory(file, description)
}
package com.example.storyapp_androidintermediate_sub1.view.maps

import androidx.lifecycle.ViewModel
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository

class MapsViewModel (private val repository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
}
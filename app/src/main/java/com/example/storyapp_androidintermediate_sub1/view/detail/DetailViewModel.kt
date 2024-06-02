package com.example.storyapp_androidintermediate_sub1.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository
import com.example.storyapp_androidintermediate_sub1.retrofit.response.ListStoryItem

class DetailViewModel(private val repository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStories().cachedIn(viewModelScope)
}
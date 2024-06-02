package com.example.storyapp_androidintermediate_sub1.repository.Injection

import android.content.Context
import com.example.storyapp_androidintermediate_sub1.data.api.ApiConfig
import com.example.storyapp_androidintermediate_sub1.data.local.pref.UserPreference
import com.example.storyapp_androidintermediate_sub1.data.local.pref.dataStore
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}
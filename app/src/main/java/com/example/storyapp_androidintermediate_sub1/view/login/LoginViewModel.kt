package com.example.storyapp_androidintermediate_sub1.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp_androidintermediate_sub1.data.local.pref.UserAuth
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel (
    private val repository: StoryRepository
) : ViewModel(){
    fun login(email: String, password: String) = repository.login(email, password)
    fun saveSession(token: UserAuth) {
        viewModelScope.launch {
            repository.saveSession(token)
        }
    }
}
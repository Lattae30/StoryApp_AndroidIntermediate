package com.example.storyapp_androidintermediate_sub1.view.signup

import androidx.lifecycle.ViewModel
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository

class SignupViewModel(private val repository: StoryRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = repository.register(name, email, password)

}
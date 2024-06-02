package com.example.storyapp_androidintermediate_sub1.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp_androidintermediate_sub1.repository.Injection.Injection
import com.example.storyapp_androidintermediate_sub1.repository.StoryRepository
import com.example.storyapp_androidintermediate_sub1.view.add.AddStoryViewModel
import com.example.storyapp_androidintermediate_sub1.view.detail.DetailViewModel
import com.example.storyapp_androidintermediate_sub1.view.login.LoginViewModel
import com.example.storyapp_androidintermediate_sub1.view.main.MainViewModel
import com.example.storyapp_androidintermediate_sub1.view.maps.MapsViewModel
import com.example.storyapp_androidintermediate_sub1.view.signup.SignupViewModel

class ViewModelFactory (private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)){
            return SignupViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)){
            return MapsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideStoryRepository(context)).also { INSTANCE = it }
            }
        }
    }
}
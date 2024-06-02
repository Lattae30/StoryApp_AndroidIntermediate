package com.example.storyapp_androidintermediate_sub1.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp_androidintermediate_sub1.data.StoryPagingSource
import com.example.storyapp_androidintermediate_sub1.data.api.ApiConfig
import com.example.storyapp_androidintermediate_sub1.data.api.ApiService
import com.example.storyapp_androidintermediate_sub1.data.local.pref.UserAuth
import com.example.storyapp_androidintermediate_sub1.data.local.pref.UserPreference
import com.example.storyapp_androidintermediate_sub1.retrofit.response.AddStoryResponse
import com.example.storyapp_androidintermediate_sub1.retrofit.response.ListStoryItem
import com.example.storyapp_androidintermediate_sub1.retrofit.response.RegisterResponse
import com.example.storyapp_androidintermediate_sub1.retrofit.response.StoryResponse
import com.example.storyapp_androidintermediate_sub1.retrofit.result.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){

    suspend fun saveSession(token: UserAuth) {
        userPreference.saveSession(token)
    }

    fun getSession(): Flow<UserAuth> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            val userAuth = UserAuth(
                token = successResponse.loginResult?.token ?: "",
                isLogin = true
            )
            userPreference.saveSession(userAuth)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>>{
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    val user = runBlocking { userPreference.getSession().first() }
                    val apiService = ApiConfig.getApiService(user.token)
                    StoryPagingSource(apiService)
                }
            ).liveData
    }

    fun getStoriesWithLocation(): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.getStoriesWithLocation()
            val stories = successResponse.listStory
            val storiesList = stories.map {
                ListStoryItem(
                    it?.photoUrl,
                    it?.createdAt,
                    it?.name,
                    it?.description,
                    it?.lon,
                    it!!.id,
                    it?.lat
                )
            }
            emit(ResultState.Success(storiesList))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error("$errorResponse"))
        }
    }

    fun addNewStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.addNewStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref)
            }.also { instance = it }
    }
}
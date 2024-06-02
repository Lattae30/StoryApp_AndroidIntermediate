package com.example.storyapp_androidintermediate_sub1.data.local.pref

data class UserAuth(
    val token: String,
    val isLogin: Boolean = false
)
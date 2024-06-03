package com.fiknaufalh.snapstory.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
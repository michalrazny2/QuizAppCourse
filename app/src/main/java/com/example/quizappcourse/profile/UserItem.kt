package com.example.quizappcourse.profile

import java.io.Serializable

// Model of single user

data class UserItem(
    var name: String = "",
    var url: String = "",
    var uid: String = ""): Serializable
package com.example.quizappcourse

import com.example.quizappcourse.profile.UserItem
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserItem(): UserItem {
    return UserItem().apply{
        uid = this@toUserItem.uid
        url = this@toUserItem.photoUrl.toString()
        name = this@toUserItem.displayName!!
    }
}
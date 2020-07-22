package com.example.quizappcourse.chooser

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.quizappcourse.R
import com.example.quizappcourse.QApp

enum class LangEnum(@StringRes val label: Int,
                    @DrawableRes val image: Int) {
    ANDROID(R.string.lang_android, R.drawable.ic_language_android),
    JAVA(R.string.lang_java, R.drawable.ic_language_java),
    KOTLIN(R.string.lang_kotlin, R.drawable.ic_language_kotlin);

    fun getString() = {
        QApp.res.getString(this.label)
    }
}
package com.example.quizappcourse.chooser

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R

enum class LevelEnum(@StringRes val label: Int,
                     @DrawableRes val image: Int){
    EASY(R.string.level_easy, R.drawable.ic_level_easy),
    AVARAGE(R.string.level_average, R.drawable.ic_level_average),
    HARD(R.string.level_hard, R.drawable.ic_level_hard);

    fun getString () ={
        QApp.res.getString(this.label)
    }
}
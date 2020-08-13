package com.example.quizappcourse.chooser

import java.io.Serializable

/* Single model of a quiz
* */
data class QuizItem(
    var level : LevelEnum = LevelEnum.EASY,
    var lang: LangEnum = LangEnum.ANDROID,
    var questset: String = "") : Serializable

package com.example.quizappcourse.quiz

import java.io.Serializable


data class QuestionItem(
    var ask: String = "",
    var positive: String = "",
    var false1: String = "",
    var false2: String = ""
):Serializable
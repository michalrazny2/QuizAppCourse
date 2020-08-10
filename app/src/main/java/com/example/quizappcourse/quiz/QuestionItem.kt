package com.example.quizappcourse.quiz

import java.io.Serializable


data class QuestionItem(
    var ask: String = "",
    var positive: String = "pos",
    var false1: String = "fal1",
    var false2: String = "fal2"
):Serializable
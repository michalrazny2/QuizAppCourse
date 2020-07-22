package com.example.quizappcourse.news

import java.io.Serializable

// Model for a single news item, concious database denormalization

data class NewsItem (
    var comment: String = "",
    var points: Int = 0,
    var quiz: String = "", //id/name of the completed quiz
    var image: String="",  //path to image
    var user: String="",
    var timeMilis: Long = 0, //time of quiz completion
    var uid: String = "",
    var respects: HashMap<String,Int> = hashMapOf()  //if it was 0, we couldnt unlike this
 ) : Serializable{}
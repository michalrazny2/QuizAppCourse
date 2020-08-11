package com.example.quizappcourse.summary

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quizappcourse.MainActivity.Companion.USER_NAME
import com.example.quizappcourse.MainActivity.Companion.USER_URL
import com.example.quizappcourse.R
import com.example.quizappcourse.quiz.QuizActivity.Companion.POINTS
import com.example.quizappcourse.quiz.QuizActivity.Companion.QUIZ_NAME
import com.example.quizappcourse.quiz.QuizActivity.Companion.SUCCESS_SUMMARY
import kotlinx.android.synthetic.main.fragment_newsitem.*
import kotlinx.android.synthetic.main.fragment_newsitem.quizTitle
import kotlinx.android.synthetic.main.fragment_quizitem.*
import kotlinx.android.synthetic.main.fragment_quizitem_list.*
import kotlinx.android.synthetic.main.result_activity.*

// Klasa obslugujaca podsumowanie quizu bo zakonczeniu go przez uzytkownika
class QuizSummaryActivity : AppCompatActivity() {

    // intent extras
    private val quiz_name by lazy{intent.extras?.get(QUIZ_NAME) as String}
    private val success_summary by lazy{intent.extras?.get(SUCCESS_SUMMARY) as String}
    private val points by lazy{intent.extras?.get(POINTS) as Int}
    private val user_name by lazy{intent.extras?.get(USER_NAME) as String}
    private val user_url by lazy{intent.extras?.get(USER_URL) as String}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        setUpViews()
    }

    private fun setUpViews() {
        title_caption.text = success_summary
        quizTitle.text = quiz_name
        pointsText.text = points.toString()
        respects.text = 1.toString()
        time.text = "00m"

        setUserName()
        setUserImage()
        setAddComment()

        likesImage.isEnabled = false
        setUpOkButton()
        setUpCloseButton()

    }

    private fun setUserName() {
        if(!user_name.isNullOrEmpty()){
            name.text = user_name
        }
    }

    private fun setUserImage() {
        if(!user_url.isNullOrEmpty()){
            Glide.with(this)  // uzycie Glide
                .load(user_url)
                .into(circleImageProfile)
        }
    }

    // comment
    private fun setAddComment() {
        TODO("Not yet implemented")
    }

    // score sharing
    private fun setUpOkButton() {
        TODO("Not yet implemented")
    }

    private fun setUpCloseButton() {
        TODO("Not yet implemented")
    }

}
package com.example.quizappcourse.summary

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.quizappcourse.BaseActivity
import com.example.quizappcourse.MainActivity.Companion.USER_NAME
import com.example.quizappcourse.MainActivity.Companion.USER_URL
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R
import com.example.quizappcourse.news.NewsItem
import com.example.quizappcourse.quiz.QuizActivity.Companion.POINTS
import com.example.quizappcourse.quiz.QuizActivity.Companion.QUIZ_NAME
import com.example.quizappcourse.quiz.QuizActivity.Companion.SUCCESS_SUMMARY
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_newsitem.*
import kotlinx.android.synthetic.main.fragment_newsitem.quizTitle
import kotlinx.android.synthetic.main.fragment_quizitem.*
import kotlinx.android.synthetic.main.fragment_quizitem_list.*
import kotlinx.android.synthetic.main.result_activity.*

// Klasa obslugujaca podsumowanie quizu bo zakonczeniu go przez uzytkownika
class QuizSummaryActivity : BaseActivity() {

    // intent extras
    private val quiz_name by lazy{intent.extras?.get(QUIZ_NAME) as String}
    private val success_summary by lazy{intent.extras?.get(SUCCESS_SUMMARY) as String}
    private val pointed by lazy{intent.extras?.get(POINTS) as Int}
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
        pointsText.text = pointed.toString()
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
        add_comment.visibility = View.VISIBLE
        comment.visibility = View.GONE
        add_comment.setOnClickListener{
            v -> showEditComment()
        }
    }

    private fun showEditComment() {
        add_comment.visibility = View.GONE
        edit_comment.visibility = View.VISIBLE
    }

    // score sharing
    private fun setUpOkButton() {
        if(FirebaseAuth.getInstance().currentUser != null){
            ok.setOnClickListener{
                v -> goToPublish()
            }
            }else{
            ok.text = QApp.res.getString(R.string.not_logged_news)
            ok.setOnClickListener{ logIn() }
        }
    }

    private fun goToPublish() {
        val intent = Intent().apply{
            putExtra(NEW_FEED, NewsItem().apply{
                comment = edit_comment.text.toString()
                points = pointed
                quiz = quiz_name
                timeMilis = System.currentTimeMillis()
            })
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onLogInSuccess() {
        goToPublish()
    }

    override fun onLogInFailure(exception: Exception?){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun setUpCloseButton() {
        close_btn.setOnClickListener{
            v -> setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    companion object{
        const val NEW_FEED = "newFeed"
    }

}
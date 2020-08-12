package com.example.quizappcourse

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.quizappcourse.chooser.QuizChooserFragment
import com.example.quizappcourse.chooser.QuizItem
import com.example.quizappcourse.news.NewsListFragment
import com.example.quizappcourse.profile.OtherProfileActivity
import com.example.quizappcourse.profile.ProfileFragment
import com.example.quizappcourse.profile.UserItem
import com.example.quizappcourse.quiz.QuestionItem
import com.example.quizappcourse.quiz.QuizActivity
import com.example.quizappcourse.summary.QuizSummaryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_quizitem_list.*

/* Main activity of application
* */

class MainActivity : AppCompatActivity(), QuizChooserFragment.OnStartQuizListener, NewsListFragment.OnNewsIteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViewPager()
    }


    private fun setViewPager()  {
        viewpager.adapter = getFragmentPagerAdapter()
        navigation.setOnNavigationItemSelectedListener(getBottomNavigationItemSelectedListener())
        viewpager.addOnPageChangeListener(getOnPageChangeListener())
        viewpager.offscreenPageLimit = 2
    }

    private fun getFragmentPagerAdapter() =
        object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int) = when(position) {
                FEED_ID -> NewsListFragment() //News List Fragment
                CHOOSER_ID -> QuizChooserFragment() //Quiz chooser fragment
                PROFILE_ID -> ProfileFragment.newInstance(UserItem("user_test", "https://www.xdxdxd.pl", "asdf")) // profile fragment
                else -> {
                        Log.wtf("Fragment out of bonds", "How come?")
                        Fragment()
                        }
            }

            override fun getCount() = 3

        }

    private fun getBottomNavigationItemSelectedListener() =
    BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                viewpager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                viewpager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications ->{
                viewpager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            else -> return@OnNavigationItemSelectedListener false
        }
    }


    private fun getOnPageChangeListener()=
        object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }

        }

    companion object{
        const val FEED_ID = 0
        const val CHOOSER_ID = 1
        const val  PROFILE_ID = 2

        const val QUIZ_SET = "quiz_set"
        const val QUIZ = "quiz"
        const val TITLE = "title"
        const val QUIZ_ACT_REQ_CODE = 100
        const val QUIZ_SUMMARY_CODE = 101

        const val USER_URL = "USER_URL"
        const val USER_NAME = "USER_NAME"
        const val USER_ITEM = "USER_ITEM"
    }

    private fun getChooserListFragment() = (supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.viewpager+":"+ CHOOSER_ID) as QuizChooserFragment)

    override fun onStartQuizSelected(quiz: QuizItem, name: String) {
        Log.i("MAIN ACTIVITY", "Main activity on start quiz selected ")
        getChooserListFragment().loader_quiz.visibility = View.VISIBLE
        //todo: komunikacja
        var quizset = ArrayList<QuestionItem>().apply{
            add(QuestionItem())
            add(QuestionItem())
            add(QuestionItem())
            add(QuestionItem())
            add(QuestionItem())
        }
        navigateQuiz(quizset, name, quiz)
    }

    // obsluga wynikow z okien:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when{
                (requestCode == QUIZ_ACT_REQ_CODE)->{
                    navigateToSummaryActivity(data)
                }
                (requestCode == QUIZ_SUMMARY_CODE)->{
                    //todo wyslanie danych na serwer
                }
            }
        }
    }

    private fun navigateToSummaryActivity(data: Intent?) {
        var intent = Intent(this, QuizSummaryActivity::class.java).apply{
            if(QApp.fUser != null){
                // todo: zasilenie uzytkownikiem
                data?.putExtra(USER_NAME, "Uzytkownik")
                data?.putExtra(USER_URL, "htpps://wwww.costam.pl")
            }
            data?.extras?.let { putExtras(it) }
        }
        startActivityForResult(intent, QUIZ_SUMMARY_CODE)
    }

    private fun navigateQuiz(quizSet: ArrayList<QuestionItem>, title:String, quiz: QuizItem) {
        val intent = Intent(this, QuizActivity::class.java).apply{
            putExtra(QUIZ_SET, quizSet)
            putExtra(TITLE, title)
            putExtra(QUIZ, quiz)
        }
        startActivityForResult(intent, QUIZ_ACT_REQ_CODE)
    }

    // Funkcje z interface z NewsListFragmentu
    override fun onUserSelected(user: UserItem, image: View) {
        val intent = Intent(this, OtherProfileActivity::class.java)
        intent.putExtra(USER_ITEM, user)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, "circleProfileImageTransition")
        startActivity(intent, optionsCompat.toBundle())

    }

    override fun onLikeSelected(feedId: String, diff: Int) {
        //todo feed
    }
}
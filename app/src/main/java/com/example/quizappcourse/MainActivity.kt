package com.example.quizappcourse

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.quizappcourse.chooser.QuizChooserFragment
import com.example.quizappcourse.chooser.QuizItem
import com.example.quizappcourse.news.NewsItem
import com.example.quizappcourse.news.NewsListFragment
import com.example.quizappcourse.profile.OtherProfileActivity
import com.example.quizappcourse.profile.ProfileFragment
import com.example.quizappcourse.profile.UserItem
import com.example.quizappcourse.quiz.QuestionItem
import com.example.quizappcourse.quiz.QuizActivity
import com.example.quizappcourse.summary.QuizSummaryActivity
import com.example.quizappcourse.summary.QuizSummaryActivity.Companion.NEW_FEED
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_newsitem_list.*
import kotlinx.android.synthetic.main.fragment_quizitem_list.*

/* Main activity of application
* */

class MainActivity : BaseActivity(),
    QuizChooserFragment.OnStartQuizListener,
    NewsListFragment.OnNewsIteractionListener,
    ProfileFragment.OnLogChangeListener{
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
                PROFILE_ID -> ProfileFragment() // profile fragment
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


    override fun onStartQuizSelected(quiz: QuizItem, name: String) {
        Log.i("MAIN ACTIVITY", "Main activity on start quiz selected ")
        getChooserListFragment().loader_quiz.visibility = View.VISIBLE

        QApp.fData.getReference("questions/${quiz.questSet}")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val quizset = ArrayList<QuestionItem>()
                    // tego mapa srednio rozumiem
                    p0.children.map{it.getValue(QuestionItem::class.java)}.mapTo(quizset){it!!}
                    getChooserListFragment().loader_quiz.visibility = View.GONE
                    navigateQuiz(quizset, name, quiz)
                }

            })


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
                    pushNewNews(data)
                }
            }
        }
    }

    private fun pushNewNews(data: Intent?) {
        val feedItem = data!!.extras?.get(NEW_FEED) as NewsItem //spojne z wezlami z firebase'a
        QApp.fData.getReference("feeds").push().setValue(feedItem.apply{
            uid = QApp.fUser!!.uid
            image = QApp.fUser!!.photoUrl.toString()
            user = QApp.fUser!!.displayName!!
        })
        viewpager.currentItem = 0
        getNewsListFragment().feed_item_list.smoothScrollToPosition(0)
    }

    private fun navigateToSummaryActivity(data: Intent?) {
        var intent = Intent(this, QuizSummaryActivity::class.java).apply{
            if(QApp.fUser != null){
                // pobranie uzytkownika
                data?.putExtra(USER_NAME, QApp.fUser?.displayName ?: QApp.res.getString(R.string.anonym_name))
                data?.putExtra(USER_URL, QApp.fUser?.photoUrl.toString())
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
        // dawanie lajka
        if(QApp.fUser != null){
            QApp.fData.getReference("feeds/$feedId/respects").updateChildren(mapOf(Pair(QApp.fUser?.uid, diff)))
                .addOnCompleteListener { object: OnCompleteListener<Void>{
                    override fun onComplete(p0: Task<Void>) {
                        Log.d("MainActivity", "Just liked $feedId with $diff")
                    }

                } }
        }
    }

    override fun onLogIn() {
        logIn()  //oddelegowanie do BaseActivity
    }

    override fun onLogOut() {
        QApp.fAuth.signOut()
        getNewsListFragment().feed_item_list.adapter?.notifyDataSetChanged()
    }

    private fun getNewsListFragment() = (supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.viewpager+":"+ FEED_ID) as NewsListFragment)
    private fun getChooserListFragment() = (supportFragmentManager.findFragmentByTag("android:switcher:"+R.id.viewpager+":"+ CHOOSER_ID) as QuizChooserFragment)


}
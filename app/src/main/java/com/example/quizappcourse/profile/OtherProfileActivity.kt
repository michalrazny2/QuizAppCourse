package com.example.quizappcourse.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quizappcourse.MainActivity
import com.example.quizappcourse.R
import com.example.quizappcourse.news.NewsListFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.other_profile_activity.view.*

class OtherProfileActivity : AppCompatActivity(), NewsListFragment.OnNewsIteractionListener{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.extras?.get(MainActivity.USER_ITEM) as UserItem
        setContentView(R.layout.other_profile_activity)
        val ft = supportFragmentManager.beginTransaction() //todo: co to wlasicwie jest??
        ft.add(R.id.layout_other_profile, ProfileFragment.newInstance(user)).commit()

    }

    override fun onStart() {
        super.onStart()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        toolbar.navigationIcon = R.drawable.ic_close as Drawable //todo to do zmiany pewnie
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onUserSelected(user: UserItem, image: View) {
        //niedostepne w tym oknie
    }

    override fun onLikeSelected(feedId: String, diff: Int) {
        TODO("Not yet implemented") // like feed
    }


}
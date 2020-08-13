package com.example.quizappcourse.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quizappcourse.BaseActivity
import com.example.quizappcourse.MainActivity
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R
import com.example.quizappcourse.news.NewsListFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.other_profile_activity.view.*

class OtherProfileActivity : BaseActivity(), NewsListFragment.OnNewsIteractionListener{



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
        toolbar.setNavigationIcon(R.drawable.ic_close)  //todo to do zmiany pewnie
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onUserSelected(user: UserItem, image: View) {
        //niedostepne w tym oknie
    }

    override fun onLikeSelected(feedId: String, diff: Int) {
        // dawanie lajka
        if(QApp.fUser != null){
            QApp.fData.getReference("feeds/$feedId/respects").updateChildren(mapOf(Pair(QApp.fUser?.uid, diff)))
                .addOnCompleteListener { object: OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        Log.d("MainActivity", "Just liked $feedId with $diff")
                    }

                } }
        }
    }


}
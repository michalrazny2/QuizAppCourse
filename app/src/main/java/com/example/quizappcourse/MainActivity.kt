package com.example.quizappcourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

/* Main activity of application
* */

class MainActivity : AppCompatActivity() {
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
                FEED_ID -> Fragment() //News List Fragment
                CHOOSER_ID -> Fragment() //Quiz chooser fragment
                PROFILE_ID -> Fragment() // profile fragment
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
    }
}
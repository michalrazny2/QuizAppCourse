package com.example.quizappcourse.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R
import com.example.quizappcourse.news.NewsItem
import com.example.quizappcourse.news.NewsListFragment
import com.example.quizappcourse.news.NewsListRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    var currentUser : UserItem? = null

    private var mNewsMap: HashMap<String, NewsItem> = hashMapOf(
        //todo: do usuniecia po dodaniu polaczenia z internetem
        Pair("Asff", NewsItem(comment="komentarzyk wow",
            points= 10,
            quiz="Kotlin łatwy",
            image= "https://www.costam.pl",
            user = "Ziomka ziomekdsaddad",
            timeMilis = System.currentTimeMillis()-1000,
            uid = "asdsad",
            respects = hashMapOf())),

        Pair("Akjhkff", NewsItem(comment="komentarz",
            points= 10,
            quiz="Kotlin łatwy",
            user = "Ziomka ziomek",
            timeMilis = System.currentTimeMillis()-1000,
            uid = "asdsad",
            respects = hashMapOf())),

        Pair("Asdaddasf", NewsItem(comment="komentarz",
            points= 10,
            quiz="Kotlin łatwy",
            image= "https://www.costam.pl",
            user = "Ziomka ziomek",
            timeMilis = System.currentTimeMillis()-1000,
            uid = "asdsad",
            respects = hashMapOf())),

        Pair("Asfbnvbbvnf", NewsItem(comment="komentarzyk wow",
            points= 10,
            quiz="Kotlin łatwy",
            image= "https://www.costam.pl",
            user = "Ziomka ziomekdsaddad",
            timeMilis = System.currentTimeMillis()-1000,
            uid = "asdsad",
            respects = hashMapOf())),

        Pair("Asnbvnff", NewsItem(comment="komentarzyk wow",
            points= 10,
            quiz="Kotlin łatwy",
            image= "https://www.costam.pl",
            user = "Ziomka ziomekdsaddad",
            timeMilis = System.currentTimeMillis()-1000,
            uid = "asdsad",
            respects = hashMapOf()))
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecycler()
    }

    private fun setUpRecycler() {
        if(context is NewsListFragment.OnNewsIteractionListener){
            feed_recycler.layoutManager = LinearLayoutManager(context)
            feed_recycler.adapter = NewsListRecyclerViewAdapter(mNewsMap, context as NewsListFragment.OnNewsIteractionListener)
        }
    }

    override fun onStart() {
        super.onStart()
        //start auth listener
        //update current user
        getCurrentUser()
        //updateFeedRefEventListener
        updateLogOn()
    }

    private fun getCurrentUser() {
        currentUser = arguments?.get(USER) as UserItem
        if(currentUser == null){
            //todo pobranie uzytkownika z autentykacji
        }
    }

    private fun updateLogOn() {
        when{
            currentUser == null -> {
                setUpViewNotLogged()
                sign_in_button.setOnClickListener{
                    //todo: logowanie
                    loader_profil.visibility = View.VISIBLE
                }
            }
            currentUser != null ->{
                setUpViewsLogged()
            }
        }
    }

    private fun setUpViewsLogged() {
        login_layout.visibility = View.GONE
        feed_recycler.visibility = View.VISIBLE
        setUpUserData()
    }

    private fun setUpUserData() {
        collapsing_toolbar.title = currentUser!!.name
        Glide.with(this@ProfileFragment)
            .load(currentUser?.url)
            .into(circleProfileImage)
    }

    private fun setUpViewNotLogged() {
        login_layout.visibility = View.VISIBLE
        feed_recycler.visibility = View.GONE
        respects.text = 0.toString()
        points.text = 0.toString()
        circleProfileImage.setImageDrawable(QApp.res.getDrawable(R.drawable.ic_anonym_face, null))
        collapsing_toolbar.title = QApp.res.getString(R.string.anonym_name)
    }

    override fun onStop() {
        super.onStop()
        //stop auth listener
    }

    companion object{
        const val USER = "user"

        fun newInstance(user: UserItem): ProfileFragment{
            val fragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putSerializable(USER,user)
            fragment.arguments = bundle
            return fragment
        }
    }

}
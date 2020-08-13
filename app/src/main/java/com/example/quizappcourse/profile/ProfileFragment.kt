package com.example.quizappcourse.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.quizappcourse.MainActivity
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R
import com.example.quizappcourse.news.NewsItem
import com.example.quizappcourse.news.NewsListFragment
import com.example.quizappcourse.news.NewsListRecyclerViewAdapter
import com.example.quizappcourse.toUserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    var feedRef: Query? = null
    var currentUser : UserItem? = null
    var respectValue = 1
    val authListener: FirebaseAuth.AuthStateListener by lazy{
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            if(firebaseAuth.currentUser != null){
                updateCurrentUser()
                updateFeedRefEventListener() //todo tak jak w onpause/onstart
                updateLogOn()
            }else{
                Log.d("NewsListFragment", "authListener-usernull")
            }
        }
    }

    val eventListener: ValueEventListener by lazy{
        object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
               var pointsValue = 0
                respectValue = 0
                for(it in p0.children){
                    val feed = it.getValue(NewsItem::class.java)!!
                    mNewsMap.put(it.key!!, feed)
                    pointsValue += feed.points
                    respectValue += 1 + it.child("respects").children.sumBy{it.getValue(Int::class.java)!!}

                }
                feed_recycler?.adapter?.notifyDataSetChanged()
                respects?.text = respectValue.toString()
                points?.text = pointsValue.toString()
                loader_profil?.visibility = View.INVISIBLE

            }
        }
    }

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
        QApp.fAuth.addAuthStateListener { authListener }
        //update current user
        updateCurrentUser()
        updateFeedRefEventListener()
        updateLogOn()
    }

    override fun onStop() {
        super.onStop()
        //stop auth listener
        QApp.fAuth.removeAuthStateListener { authListener }
    }

    private fun updateCurrentUser() {
        currentUser = arguments?.get(USER) as? UserItem
        if(currentUser == null){
            //pobranie uzytkownika z autentykacji
            val firebase = QApp.fAuth.currentUser
            firebase?.let{
                currentUser = firebase.toUserItem()
            }
        }
    }

    private fun updateLogOn() {
        when{
            currentUser == null -> {
                setUpViewNotLogged()
                sign_in_button.setOnClickListener{
                    (activity as OnLogChangeListener).onLogIn()
                    loader_profil.visibility = View.VISIBLE
                }
            }
            currentUser != null ->{
                setUpViewsLogged()
            }
        }
        updateDebugFabLogout()
    }

    private fun updateDebugFabLogout() {
        if(BuildConfig.DEBUG){
            val visibility = (currentUser!=null  && context is MainActivity)
            fab_debug_logout.visibility = if(visibility) View.VISIBLE else View.GONE
            fab_debug_logout.setOnClickListener {
                currentUser = null
                (activity as OnLogChangeListener).onLogOut()
                updateLogOn()
            }
        }
    }

    private fun updateFeedRefEventListener() {
        currentUser?.let{
            feedRef = FirebaseDatabase.getInstance().getReference("feeds")
                .orderByChild("uid")
                .equalTo(it.uid)

            loader_profil?.visibility = View.VISIBLE
            feedRef?.addValueEventListener(eventListener)
        }
        loader_profil?.visibility = View.INVISIBLE

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

    // interface przekazujacy akcje logowania z profilu na aktywnosc
    interface OnLogChangeListener{
        fun onLogOut()
        fun onLogIn()
    }
}
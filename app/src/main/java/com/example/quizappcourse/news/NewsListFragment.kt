package com.example.quizappcourse.news

import android.content.Context
import android.os.Bundle
import android.renderscript.Sampler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R
import com.example.quizappcourse.profile.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_newsitem_list.*

class NewsListFragment : Fragment() {
    //todo: naiwne dane
    // todo element listy
    // todo widok
    // todo inicjalizacja okna
    private lateinit var onNewsInteractionListener: NewsListFragment.OnNewsIteractionListener
    private val mNewsMap:HashMap<String, NewsItem> = hashMapOf()
    val feedsRef: Query? = FirebaseDatabase.getInstance().getReference("feeds")
    val authListener : FirebaseAuth.AuthStateListener by lazy{
        FirebaseAuth.AuthStateListener {
            firebaseAuth ->
            if(firebaseAuth.currentUser != null){
                feed_item_list.adapter?.notifyDataSetChanged()
            }
        }
    }

    lateinit var feedChangeListener:ValueEventListener //dlaczego lateinit var/ val tutaj??
    val eventListener: ValueEventListener by lazy{
        object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                //gdy data sie zmienia to:
                onUpdateRecyclerAdapter(p0)
            }

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnNewsIteractionListener){
            onNewsInteractionListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_newsitem_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loader_news.visibility = View.VISIBLE

        //gdy aktywnosc jest stworzona zaciagamy wszystkie nowinki, ale potem juz tylko aktualizujemy
        feedsRef?.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                loader_news.visibility = View.GONE
                onUpdateRecyclerAdapter(p0)
                feed_item_list.scheduleLayoutAnimation()
            }

        })
        setUpRecycler()
    }

    override fun onResume() {
        super.onResume()

        feedChangeListener = feedsRef!!.addValueEventListener(eventListener)
        QApp.fAuth.addAuthStateListener { authListener }
    }

    override fun onStop() {
        super.onStop()
        feedsRef?.removeEventListener(feedChangeListener)
        QApp.fAuth.removeAuthStateListener { authListener }
    }

    private fun onUpdateRecyclerAdapter(dataSnapshot: DataSnapshot) {
        for(it in dataSnapshot.children){
            val news = it.getValue<NewsItem>(NewsItem::class.java)!!
            mNewsMap.put(it.key!!, news)
        }
        feed_item_list?.adapter?.notifyDataSetChanged()
    }

    private fun setUpRecycler() {
        feed_item_list.layoutManager = LinearLayoutManager(context)
        feed_item_list.adapter = NewsListRecyclerViewAdapter(mNewsMap, onNewsInteractionListener)
    }

    interface OnNewsIteractionListener{
        // z kazdego elementu listy newsow onUserSelected
        fun onUserSelected(user: UserItem, image: View)
        fun onLikeSelected(feedId:String, diff:Int)
    }

}
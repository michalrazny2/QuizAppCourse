package com.example.quizappcourse.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappcourse.R
import com.example.quizappcourse.profile.UserItem
import kotlinx.android.synthetic.main.fragment_newsitem_list.*

class NewsListFragment : Fragment() {
    //todo: naiwne dane
    // todo element listy
    // todo widok
    // todo inicjalizacja okna
    private lateinit var onNewsInteractionListener: NewsListFragment.OnNewsIteractionListener
    private val mNewsMap:HashMap<String, NewsItem> = hashMapOf()

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
        //todo: ladowanie danych z internetu
//        onUpdateAdapter()

        // Naiwne dane wrzucone do hashMapy w tym momencie:
        mNewsMap.apply{
            put("Asff", NewsItem(comment="komentarzyk wow",
                points= 10,
                quiz="Kotlin łatwy",
                image= "https://www.costam.pl",
                user = "Ziomka ziomekdsaddad",
                timeMilis = System.currentTimeMillis()-1000,
                uid = "asdsad",
                respects = hashMapOf()))

            put("Akjhkff", NewsItem(comment="komentarz",
                points= 10,
                quiz="Kotlin łatwy",
                user = "Ziomka ziomek",
                timeMilis = System.currentTimeMillis()-1000,
                uid = "asdsad",
                respects = hashMapOf()))

            put("Asdaddasf", NewsItem(comment="komentarz",
                points= 10,
                quiz="Kotlin łatwy",
                image= "https://www.costam.pl",
                user = "Ziomka ziomek",
                timeMilis = System.currentTimeMillis()-1000,
                uid = "asdsad",
                respects = hashMapOf()))

            put("Asfbnvbbvnf", NewsItem(comment="komentarzyk wow",
                points= 10,
                quiz="Kotlin łatwy",
                image= "https://www.costam.pl",
                user = "Ziomka ziomekdsaddad",
                timeMilis = System.currentTimeMillis()-1000,
                uid = "asdsad",
                respects = hashMapOf()))

            put("Asnbvnff", NewsItem(comment="komentarzyk wow",
                points= 10,
                quiz="Kotlin łatwy",
                image= "https://www.costam.pl",
                user = "Ziomka ziomekdsaddad",
                timeMilis = System.currentTimeMillis()-1000,
                uid = "asdsad",
                respects = hashMapOf()))
        }
        feed_item_list.scheduleLayoutAnimation()
        loader_news.visibility = View.GONE
        setUpRecycler()
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
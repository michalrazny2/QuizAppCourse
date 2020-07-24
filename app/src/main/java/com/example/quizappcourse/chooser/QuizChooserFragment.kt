package com.example.quizappcourse.chooser

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizappcourse.R
import kotlinx.android.synthetic.main.fragment_quizitem_list.*

class QuizChooserFragment : Fragment() {

    // todo: komunikacja do zrobienia

    private lateinit var onStartQuizListener: QuizChooserFragment.OnStartQuizListener
    private val quizzesMap: HashMap<String,QuizItem> = HashMap()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnStartQuizListener){
            onStartQuizListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quizitem_list, container, false)
    }

    // Recyclerview wypelniony itemami <?>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        //todo: doimplementowac
        setCommunication()
    }

    private fun setCommunication() {
        quizzesMap.apply{
            put("1", QuizItem())
            put("12", QuizItem())
        }
    }

    private fun setUpRecyclerView(){
        //todo doimplementowac
        quest_item_list.layoutManager = GridLayoutManager(context, COLUMN_COUNT) //TODO: poprawic , blad wynika z tego ze korzystam z innych wersji
        quest_item_list.adapter = QuizChooserRecyclerViewAdapter(quizzesMap, onStartQuizListener)

    }

    interface OnStartQuizListener{
        fun onStartQuizSelected(quiz: QuizItem, string:String)
    }


    companion object{
        private const val COLUMN_COUNT = 3
    }


}
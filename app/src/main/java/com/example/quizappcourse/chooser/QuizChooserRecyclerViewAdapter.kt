package com.example.quizappcourse.chooser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappcourse.R

class QuizChooserRecyclerViewAdapter(private val quizzesMap: HashMap<String,QuizItem>,
                                    private val onStartQuizListener:QuizChooserFragment.OnStartQuizListener): RecyclerView.Adapter<QuizChooserRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val zView:View):RecyclerView.ViewHolder(zView){
        val levelImageView = zView.findViewById<View>(R.id.levelImageView) as ImageView
        val langImageView = zView.findViewById<View>(R.id.langImageView) as ImageView
        val quizTitle = zView.findViewById<View>(R.id.quizTitle) as TextView

        //model danych warto miec we ViewHolderze gdyby kiedys byl potrzebny
        lateinit var nItem: QuizItem
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_quizitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount()= quizzesMap.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sorted = quizzesMap.values.toList().sortedBy { quizItem -> (quizItem.level.ordinal + quizItem.lang.ordinal*10) }

        holder.nItem = sorted[position]

        holder.levelImageView.setImageResource(sorted[position].level.image)
        holder.langImageView.setImageResource(sorted[position].lang.image)
        holder.quizTitle.text = getDoubleLineQuizTitle(sorted,position)

        holder.zView.setOnClickListener{
            onStartQuizListener.onStartQuizSelected(holder.nItem,getSingleLineQuizTitle(sorted,position))
        }
    }
    private fun  getSingleLineQuizTitle(sorted: List<QuizItem>,position:Int)
            = "${sorted[position].lang.getString()} \n ${sorted[position].level.getString()}"

    private fun  getDoubleLineQuizTitle(sorted: List<QuizItem>,position:Int)
        = "${sorted[position].lang.getString()} \n ${sorted[position].level.getString()}"

}
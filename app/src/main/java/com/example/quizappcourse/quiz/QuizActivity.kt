package com.example.quizappcourse.quiz

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizappcourse.R
import com.example.quizappcourse.chooser.QuizItem
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.quiz_activity.*
import java.util.*
import kotlin.collections.ArrayList

class QuizActivity : AppCompatActivity(){
    // nowa aktywnosc- bo otwieramy nowe okno

    //potrzebujemy kolekcje do obsługi pytan:
    private val questionList by lazy{intent.extras.get(QUIZ_SET) as ArrayList<QuestionItem>}
    private val quizIterator by lazy{questionList.iterator()}
    private lateinit var currentQuestionItem: QuestionItem
    private var currentPositive = 0
    private var currentNumber : Int = 0
        get() = if(field<5) field else 4

    private val quizTitle by lazy{
        intent.extras.get(TITLE) as String
    }
    private val quiz by lazy{
        // quiz jest inicjalizowany leniwie- co oznacza ze ten blok kodu wykonuje sie
        // przy pierwszym wywolaniu val quiz
        intent.extras.get(QUIZ) as QuizItem

    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.quiz_activity)

        quizLogo.setImageResource(quiz.lang.image)
        levelImageView.setImageResource(quiz.level.image)
        nextQuestion()
    }

    private fun nextQuestion() {
        if(quizIterator.hasNext()){
            currentQuestionItem = quizIterator.next()
            currentPositive = Random().nextInt(3)+1 // na ktorej pozycji jest poprawna odpowiedz
            progress.setCurrentStateNumber(StateProgressBar.StateNumber.values()[currentNumber])
            // todo: bardziej ogarniete ustawianie pyatania
            setUpButtons()
            // todo: uruchomienie odliczania

        }
        else{
            returnResultFromQuiz() //jesli koniec pytan to zwrocenie wyniku
        }
    }

}
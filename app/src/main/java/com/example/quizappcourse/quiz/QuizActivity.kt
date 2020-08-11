package com.example.quizappcourse.quiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.example.quizappcourse.MainActivity.Companion.QUIZ
import com.example.quizappcourse.MainActivity.Companion.QUIZ_SET
import com.example.quizappcourse.MainActivity.Companion.TITLE
import com.example.quizappcourse.QApp
import com.example.quizappcourse.R
import com.example.quizappcourse.chooser.QuizItem
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.quiz_activity.*
import java.util.*
import kotlin.collections.ArrayList

class QuizActivity : AppCompatActivity(){
    //TODO: do poprawy ten blad ktory powoduje ze wyswietlaja sie jakies losowe napisy w okolicach quizchooserfragmentu/quizitemu

    // nowa aktywnosc- bo otwieramy nowe okno

    //potrzebujemy kolekcje do obsługi pytan:
    private val questionList by lazy{ intent.extras!!.get(QUIZ_SET) as ArrayList<QuestionItem>}
    private val quizIterator by lazy{questionList.iterator()}
    val succesArray: BooleanArray by lazy{BooleanArray(questionList.size)}

    private lateinit var currentQuestionItem: QuestionItem
    private var currentPositive = 0
    private var currentNumber : Int = 0
        get() = if(field<5) field else 4

    var countDown = getCountDownTimer()
    var prepareNext = getPrepareNextTimer()

    private val quizTitle by lazy{
        intent.extras!!.get(TITLE) as String
    }
    private val quiz by lazy{
        // quiz jest inicjalizowany leniwie- co oznacza ze ten blok kodu wykonuje sie
        // przy pierwszym wywolaniu val quiz
        intent.extras!!.get(QUIZ) as QuizItem

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
            questionText.niceSetText(currentQuestionItem.ask)
            setUpButtons()
            // todo: uruchomienie odliczania
            countDown.start() //start timera

        }
        else{
            returnResultFromQuiz() //jesli koniec pytan to zwrocenie wyniku
        }
    }

    private fun setUpButtons() {
        ans_a.setOnClickListener{onChoiceListener(false)}
        ans_b.setOnClickListener{onChoiceListener(false)}
        ans_c.setOnClickListener{onChoiceListener(false)}
        when(currentPositive){
            1->{
                ans_a.niceSetText(currentQuestionItem.positive)
                ans_a.setOnClickListener(onChoiceListener(true))
                ans_b.niceSetText(currentQuestionItem.false1)
                ans_c.niceSetText(currentQuestionItem.false2)
            }
            2->{
                ans_a.niceSetText(currentQuestionItem.false1)
                ans_b.niceSetText(currentQuestionItem.positive)
                ans_b.setOnClickListener(onChoiceListener(true))
                ans_c.niceSetText(currentQuestionItem.false2)
            }
            3->{
                ans_a.niceSetText(currentQuestionItem.false1)
                ans_b.niceSetText(currentQuestionItem.false2)
                ans_c.niceSetText(currentQuestionItem.positive)
                ans_c.setOnClickListener(onChoiceListener(true))
            }
        }
    }

    private fun onChoiceListener(isPositive: Boolean):View.OnClickListener {
        return View.OnClickListener {
            // tworzymy tablice odpowiedzi
            succesArray[currentNumber] = isPositive
            countDown.cancel()
            if (!isPositive){
                // ustawienie czerwonego koloru przycisku jesli zla odpowiedz
                setButtonColorsBrand(DefaultBootstrapBrand.DANGER)
            }
            when(currentPositive){
              1-> ans_a.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
              2-> ans_b.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
              3-> ans_c.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
            }
            setButtonClickable(false) //po wybraniu odpowiedzi niepozwalamy juz na klikanie

            resetNextTimer()
            prepareNext.start()
        }
    }

    private fun setButtonClickable(clickable: Boolean) {
        ans_a.isClickable = clickable
        ans_b.isClickable = clickable
        ans_c.isClickable = clickable
    }

    private fun setButtonColorsBrand(brand: DefaultBootstrapBrand) {
        ans_a.bootstrapBrand = brand
        ans_b.bootstrapBrand = brand
        ans_c.bootstrapBrand = brand
    }

    private fun TextView.niceSetText(string:String){
        if(currentNumber>0){
            val anim = AlphaAnimation(1.0f,0.0f)
            anim.duration = 200
            anim.repeatCount = 1
            anim.repeatMode = Animation.REVERSE
            anim.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                    this@niceSetText.text = string
                }

                override fun onAnimationEnd(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {}
            })
        }else{
            this.text = string
        }
    }

    private var countDownRemain: Long = COUNTDOWNREMAIN

    private fun getCountDownTimer(): CountDownTimer {
        return object : CountDownTimer(countDownRemain, 100){
            override fun onFinish() {
                resetNextTimer()
                succesArray[currentNumber] = false
                setButtonColorsBrand(DefaultBootstrapBrand.DANGER)
                setButtonClickable(false)
                prepareNext.start()
                Toast.makeText(this@QuizActivity, QApp.res.getString(R.string.time_is_up), Toast.LENGTH_SHORT).show()
            }

            override fun onTick(remain: Long) {
                countDownRemain = remain
                timeLeftProgress.progressValue = remain / 1000f
            }

        }
    }

    private fun resetNextTimer() {
        countDownRemain = COUNTDOWNREMAIN
        prepareNextRemain = PREPARENEXTREMAIN
        prepareNext = getPrepareNextTimer()
    }

    private var prepareNextRemain: Long = PREPARENEXTREMAIN

    private fun getPrepareNextTimer(): CountDownTimer {
        return object: CountDownTimer(prepareNextRemain, 10){
            override fun onFinish() {
                resetCountDownTimer()
                setButtonClickable(true)
                setButtonColorsBrand(DefaultBootstrapBrand.SECONDARY)
                currentNumber++
                nextQuestion()
            }

            override fun onTick(remain: Long) {
                countDownRemain = remain
                timeLeftProgress.progressValue = 40 - remain.toFloat() / 50
            }

        }
    }

    private fun resetCountDownTimer() {
        countDownRemain = COUNTDOWNREMAIN
        prepareNextRemain = PREPARENEXTREMAIN
        countDown = getCountDownTimer()
    }

    // appka na pauzie (po wyjsciu z appki bez wylaczania <?>)
    override fun onPause() {
        super.onPause()
        countDown.cancel()
        prepareNext.cancel()
    }

    //po wznowieniu appki ustawiamy timery od nowa zeby appka sie nie wywalala
    override fun onResume(){
        super.onResume()
        if (!countDownRemain.equals(COUNTDOWNREMAIN)){
            countDown = getCountDownTimer()
            countDown.start()
        }
        if (!prepareNextRemain.equals(PREPARENEXTREMAIN)){
            prepareNext = getPrepareNextTimer()
            prepareNext.start()
        }
    }

    private fun returnResultFromQuiz() {
        val intent = Intent().apply(){
            putExtra(QUIZ_NAME, quizTitle)
            putExtra(SUCCESS_SUMMARY, "Poprawne ${succesArray.count({it})}/5")
            putExtra(POINTS, succesArray.count({it}) * (quiz.level.ordinal+1) *39)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object{
        const val QUIZ_NAME = "QUIZNAME"
        const val SUCCESS_SUMMARY = "SUCCESS_SUMMMARY"
        const val POINTS = "POINTS"

        const val COUNTDOWNREMAIN = 40000L
        const val PREPARENEXTREMAIN = 2000L
    }

}
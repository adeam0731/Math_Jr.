package com.example.plusminus.ui.main

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.plusminus.R

abstract class QuestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var questions: List<Question> = emptyList()
    private var count: Int = 0

    private var state: QuestionState = QuestionState.Standby

    private var counter = Counter()

    val imageList = listOf(
        R.drawable.onepiece01_luffy,
        R.drawable.onepiece02_zoro_bandana,
        R.drawable.onepiece03_nami,
        R.drawable.onepiece04_usopp_sogeking,
        R.drawable.onepiece05_sanji,
        R.drawable.onepiece06_chopper,
        R.drawable.onepiece07_robin,
        R.drawable.onepiece08_franky,
        R.drawable.onepiece09_brook,
        R.drawable.onepiece10_jinbe,
        R.drawable.onepiece11_arlong,
        R.drawable.onepiece12_buggy,
        R.drawable.onepiece14_enel,
        R.drawable.onepiece16_moria,
        R.drawable.onepiece17_doflamingo,
        R.drawable.onepiece19_kurohige_teach2,
    )

    abstract fun answerVisible(visibility: Int)

    abstract fun nextQuestion(countNo: String, question: Question)

    abstract fun onStart()
    abstract fun onFinish(sumTime: String, aveTime: String)

    abstract fun showAnswer(time: String)

    fun startQuestion(questions: List<Question>) {
        // 初期化処理
        counter.reset()
        count = 0
        this.questions = questions
        onStart()
        answerVisible(INVISIBLE)

        // １問目を表示
        nextQuestion(count)
        state = QuestionState.ShowQuestion

        // 自身を表示
        this.visibility = VISIBLE

        this.setOnClickListener {
            when (state) {
                QuestionState.Standby -> {
                    answerVisible(INVISIBLE)
                }

                QuestionState.ShowQuestion -> {
                    showAnswer(counter.stop().formatAnswerTime("かかった時間"))
                    answerVisible(VISIBLE)
                    state = QuestionState.ShowAnswer
                }

                QuestionState.ShowAnswer -> {
                    answerVisible(INVISIBLE)
                    count++
                    state = if (count < questions.size) {
                        nextQuestion(count)
                        QuestionState.ShowQuestion
                    } else {
                        onFinish(counter.sumTime().formatAnswerTime("合計"), "一問あたりの平均：%.03f秒".format(counter.aveTime()))
                        QuestionState.Finish
                    }
                }

                QuestionState.Finish -> {
                    state = QuestionState.Standby
                    this.visibility = INVISIBLE
                }
            }
        }
    }

    private fun nextQuestion(questionPosition: Int) {
        counter.start()
        val question = questions[questionPosition]
        nextQuestion("第${questionPosition + 1}問", question)
    }

    private fun Long.formatAnswerTime(prefix: String): String {
        val minutes: Long = this % (1000 * 60 * 60) / (1000 * 60)
        val seconds: Long = this % (1000 * 60) / 1000
        return String.format("%s：%02d:%02d.%01d秒", prefix, minutes, seconds, this % 1000)
    }

    data class AnswerTime(
        var startTime: Long = System.currentTimeMillis(),
        var endTime: Long = 0
    ) {

        /**
         * 回答にかかった時間
         */
        fun getTime(): Long = endTime - startTime
    }

    class Counter {
        private val answerTimes = mutableListOf<AnswerTime>()
        private var currentAnswerTime: AnswerTime? = null

        fun reset() {
            currentAnswerTime = null
            answerTimes.clear()
        }

        fun start() {
            currentAnswerTime = AnswerTime()
        }

        fun stop(): Long {
            return currentAnswerTime?.let {
                it.endTime = System.currentTimeMillis()
                answerTimes.add(it)
                it.getTime()
            } ?: 0L
        }

        fun sumTime(): Long {
            return answerTimes.sumOf { it.getTime() }
        }

        fun aveTime(): Double {
            return answerTimes.map { it.getTime() }.average() / 1000
        }
    }

    sealed class QuestionState {
        object Standby : QuestionState()
        object ShowQuestion : QuestionState()
        object ShowAnswer : QuestionState()
        object Finish : QuestionState()
    }
}

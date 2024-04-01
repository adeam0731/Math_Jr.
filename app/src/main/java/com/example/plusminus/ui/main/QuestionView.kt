package com.example.plusminus.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.plusminus.R
import com.example.plusminus.databinding.ViewQuestionBinding
import kotlin.random.Random

class QuestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    ConstraintLayout(context, attrs, defStyle) {

    private var questions: List<Question> = emptyList()
    private var count: Int = 0

    private val binding: ViewQuestionBinding

    private var state: QuestionState = QuestionState.Standby

    private val imageList = listOf(
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

    init {
        binding = ViewQuestionBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun startQuestion(questions: List<Question>) {
        // 初期化処理
        count = 0
        this.questions = questions
        binding.finish.visibility = GONE
        answerVisible(GONE)

        // １問目を表示
        nextQuestion(count)
        state = QuestionState.ShowQuestion

        // 自身を表示
        this.visibility = VISIBLE

        this.setOnClickListener {
            when (state) {
                QuestionState.Standby -> {
                    answerVisible(GONE)
                }
                QuestionState.ShowQuestion -> {
                    answerVisible(VISIBLE)
                    state = QuestionState.ShowAnswer
                }
                QuestionState.ShowAnswer -> {
                    answerVisible(GONE)
                    count++
                    if (count < questions.size) {
                        nextQuestion(count)
                        state = QuestionState.ShowQuestion
                    } else {
                        showFinishImage()
                        state = QuestionState.Finish
                    }
                }
                QuestionState.Finish -> {
                    state = QuestionState.Standby
                    this.visibility = GONE
                }
            }
        }
    }

    private fun showFinishImage(){
        binding.finish.visibility = VISIBLE
        val position = Random.nextInt(0, imageList.size)
        binding.finishImage.setImageResource(imageList[position])
    }

    private fun answerVisible(visibility: Int) {
        binding.answerLabel.visibility = visibility
        binding.answer.visibility = visibility
    }

    private fun nextQuestion(questionPosition: Int) {
        val question = questions[questionPosition]
        binding.title.text = "第${questionPosition + 1}問"

        binding.value1.text = question.question.value1.toString()
        binding.value2.text = question.question.value2.toString()
        binding.operator.text = when (question.question) {
            is QuestionItem.Minus -> "-"
            is QuestionItem.Plus -> "+"
            is QuestionItem.Multiplication -> "×"
            is QuestionItem.Division -> "÷"
        }
        binding.answer.text = when (val answer = question.answer) {
            is Answer.DivisionAnswer -> {
                if (answer.remainder == 0) {
                    binding.answer.textSize = 80f
                    answer.answer.toString()
                }else{
                    binding.answer.textSize = 40f
                    "%d あまり %d".format(answer.answer, answer.remainder)
                }
            }
            is Answer.SingleAnswer -> {
                binding.answer.textSize = 80f
                answer.answer.toString()
            }
        }
    }

    private fun finish() {

    }

    sealed class QuestionState {
        object Standby : QuestionState()
        object ShowQuestion : QuestionState()
        object ShowAnswer : QuestionState()
        object Finish : QuestionState()
    }
}
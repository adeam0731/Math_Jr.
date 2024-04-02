package com.example.plusminus.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.plusminus.databinding.ViewQuestionBinding
import kotlin.random.Random

class NationalLanguageQuestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : QuestionView(context, attrs, defStyle) {

    private val binding: ViewQuestionBinding

    init {
        binding = ViewQuestionBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun answerVisible(visibility: Int) {
        binding.answerContainer.visibility = visibility
    }

    override fun nextQuestion(countNo: String, question: Question) {
        val questionItem = question.question
        if (questionItem !is QuestionItem.Math) return
        binding.title.text = countNo
        binding.value1.text = questionItem.value1.toString()
        binding.value2.text = questionItem.value2.toString()
        binding.operator.text = when (questionItem) {
            is QuestionItem.Math.Minus -> "-"
            is QuestionItem.Math.Plus -> "+"
            is QuestionItem.Math.Multiplication -> "×"
            is QuestionItem.Math.Division -> "÷"
        }
        binding.answer.text = when (val answer = question.answer) {
            is Answer.DivisionAnswer -> {
                if (answer.remainder == 0) {
                    binding.answer.textSize = 80f
                    answer.answer.toString()
                } else {
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

    override fun onStart() {
        binding.finish.visibility = GONE
    }

    override fun onFinish(sumTime: String, aveTime: String) {
        binding.finish.visibility = VISIBLE
        val position = Random.nextInt(0, imageList.size)
        binding.finishImage.setImageResource(imageList[position])

        binding.sumTime.text = sumTime
        binding.aveTime.text = aveTime
    }

    override fun showAnswer(time: String) {
        binding.answerTime.text = time
    }
}

package com.example.plusminus.create_question

import com.example.plusminus.ui.main.Answer
import com.example.plusminus.ui.main.Question
import com.example.plusminus.ui.main.QuestionItem
import kotlin.math.min
import kotlin.random.Random

object Lv2Math {

    /**
     * 足し算の問題を生成して返す
     */
    fun createPlusQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(1, 100)
            val value1Str = value1.toString()
            val onePlace = value1Str.substring(value1Str.length - 1, value1Str.length).toInt()
            val value2 = if (onePlace == 0) {
                10
            } else {
                Random.nextInt((10 - onePlace), 10)
            }

            Question(
                QuestionItem.Math.Plus(value1, value2),
                answer = Answer.SingleAnswer(value1 + value2)
            )
        }
    }

    /**
     * 引き算の問題を生成して返す
     */
    fun createMinusQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(10, 100)
            val value1Str = value1.toString()
            val onePlace = value1Str.substring(value1Str.length - 1, value1Str.length)
            val value2 = Random.nextInt(onePlace.toInt(), 10)

            Question(
                QuestionItem.Math.Minus(value1, value2),
                answer = Answer.SingleAnswer(value1 - value2)
            )
        }
    }

    /**
     * 掛け算問題
     */
    fun createMultiplicationQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(1, 10)
            val value2 = Random.nextInt(1, 10)

            Question(
                QuestionItem.Math.Multiplication(value1, value2),
                answer = Answer.SingleAnswer(value1 * value2)
            )
        }
    }

    fun createMultiplication2Questions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(11, 100)
            val value2 = Random.nextInt(11, 100)

            Question(
                QuestionItem.Math.Multiplication(value1, value2),
                answer = Answer.SingleAnswer(value1 * value2)
            )
        }
    }

    fun createDivisionQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(1, 99)
            val value2 = Random.nextInt(1, min(value1 + 1, 10))

            val answer = value1 / value2
            val remainder = value1 % value2

            Question(
                QuestionItem.Math.Division(value1, value2),
                answer = Answer.DivisionAnswer(answer = answer, remainder = remainder)
            )
        }
    }


}
package com.example.plusminus.ui.main

data class Question(
    val question: QuestionItem,
    val answer: Answer
)

sealed class QuestionItem(open val value1: Int, open val value2: Int){
    data class Plus(override val value1: Int, override val value2: Int): QuestionItem(value1, value2)
    data class Minus(override val value1: Int, override val value2: Int): QuestionItem(value1, value2)
    data class Multiplication(override val value1: Int, override val value2: Int): QuestionItem(value1, value2)
    data class Division(override val value1: Int, override val value2: Int): QuestionItem(value1, value2)
}

sealed interface Answer{
    data class SingleAnswer(val answer: Int) : Answer
    data class DivisionAnswer(val answer: Int, val remainder: Int) : Answer
}

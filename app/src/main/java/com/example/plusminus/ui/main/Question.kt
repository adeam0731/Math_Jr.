package com.example.plusminus.ui.main

data class Question(
    val question: QuestionItem,
    val answer: Answer
)

sealed class QuestionItem{
    sealed class Math(open val value1: Int, open val value2: Int): QuestionItem() {
        data class Plus(override val value1: Int, override val value2: Int) : Math(value1, value2)
        data class Minus(override val value1: Int, override val value2: Int) : Math(value1, value2)
        data class Multiplication(override val value1: Int, override val value2: Int) : Math(value1, value2)
        data class Division(override val value1: Int, override val value2: Int) : Math(value1, value2)
    }

    sealed class NationalLanguage(): QuestionItem() {
        data class Kanji(val question: List<String>) : NationalLanguage()
    }
}

sealed interface Answer{
    data class SingleAnswer(val answer: Int) : Answer
    data class DivisionAnswer(val answer: Int, val remainder: Int) : Answer
}

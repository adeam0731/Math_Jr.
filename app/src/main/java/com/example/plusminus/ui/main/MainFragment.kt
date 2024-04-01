package com.example.plusminus.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plusminus.databinding.FragmentMainBinding
import kotlin.math.min
import kotlin.random.Random

class MainFragment : Fragment() {

    companion object {
        /** 問題数 */
        const val CREATE_COUNT = 20
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonPlus.setOnClickListener {
            val questions = createPlusQuestions(CREATE_COUNT)
            binding.questionView.startQuestion(questions)
        }
        binding.buttonMinus.setOnClickListener {
            val questions = createMinusQuestions(CREATE_COUNT)
            binding.questionView.startQuestion(questions)
        }

        binding.buttonMix.setOnClickListener {
            val plusQuestions = createPlusQuestions(CREATE_COUNT / 2)
            val minusQuestions = createMinusQuestions(CREATE_COUNT / 2)
            val list = plusQuestions.toMutableList()
            list.addAll(minusQuestions)
            list.shuffle()
            binding.questionView.startQuestion(list)
        }

        binding.buttonMultiplication.setOnClickListener {
            val questions = createMultiplicationQuestions(CREATE_COUNT)
            binding.questionView.startQuestion(questions)
        }

        binding.buttonDivision.setOnClickListener {
            val questions = createDivisionQuestions(CREATE_COUNT)
            binding.questionView.startQuestion(questions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 足し算の問題を生成して返す
     */
    private fun createPlusQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(1, 10)
            val value2 = Random.nextInt((10 - value1), 10)

            Question(
                QuestionItem.Plus(value1, value2),
                answer = Answer.SingleAnswer(value1 + value2)
            )
        }
    }

    /**
     * 引き算の問題を生成して返す
     */
    private fun createMinusQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(10, 100)
            val value1sStr = value1.toString().substring(1, 2)
            val value2 = Random.nextInt(value1sStr.toInt(), 10)

            Question(
                QuestionItem.Minus(value1, value2),
                answer = Answer.SingleAnswer(value1 - value2)
            )
        }
    }

    /**
     * 掛け算問題
     */
    private fun createMultiplicationQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(1, 10)
            val value2 = Random.nextInt(1, 10)

            Question(
                QuestionItem.Multiplication(value1, value2),
                answer = Answer.SingleAnswer(value1 * value2)
            )
        }
    }

    private fun createDivisionQuestions(createCount: Int): List<Question> {
        return (0 until createCount).map {
            val value1 = Random.nextInt(1, 99)
            val value2 = Random.nextInt(1, min(value1 + 1, 10))

            val answer = value1 / value2
            val remainder = value1 % value2

            Question(
                QuestionItem.Division(value1, value2),
                answer = Answer.DivisionAnswer(answer = answer, remainder = remainder)
            )
        }
    }
}
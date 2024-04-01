package com.example.plusminus.ui.main

import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plusminus.R
import com.example.plusminus.databinding.FragmentMainBinding
import kotlin.math.min
import kotlin.random.Random

class MainFragment : Fragment(), OnClickMenuListener {

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
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
            dividerItemDecoration.setDrawable(it)
        }

        binding.menuList.addItemDecoration(dividerItemDecoration)
        binding.menuList.layoutManager = layoutManager
        binding.menuList.adapter = MenuAdapter(requireContext()).apply {
            onClickMenuListener = this@MainFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickMenu(menu: Menu) {
        when (menu.no) {
            1 -> {
                val questions = distinctQuestions(CREATE_COUNT) { createPlusQuestions(it) }
                binding.questionView.startQuestion(questions)
            }

            2 -> {
                val questions = distinctQuestions(CREATE_COUNT) { createMinusQuestions(it) }
                binding.questionView.startQuestion(questions)
            }

            3 -> {
                val plusQuestions =
                    distinctQuestions(CREATE_COUNT / 2) { createPlusQuestions(it) }
                val minusQuestions =
                    distinctQuestions(CREATE_COUNT / 2) { createMinusQuestions(it) }
                val questions = shuffleCombine(plusQuestions, minusQuestions)
                binding.questionView.startQuestion(questions)
            }

            4 -> {
                val questions = distinctQuestions(CREATE_COUNT) { createMultiplicationQuestions(it) }
                binding.questionView.startQuestion(questions)
            }

            5 -> {
                val questions = distinctQuestions(CREATE_COUNT) { createDivisionQuestions(it) }
                binding.questionView.startQuestion(questions)
            }

            else -> {}
        }
    }


    private fun <T : Any> shuffleCombine(vararg lists: List<T>): List<T> {
        val combinedList = mutableListOf<T>()
        lists.forEach {
            combinedList.addAll(it)
        }
        combinedList.shuffle()
        return combinedList
    }

    private fun distinctQuestions(
        createCount: Int,
        create: (createCount: Int) -> List<Question>
    ): List<Question> {
        val createdQuestions = create.invoke(createCount).distinct()
        if (createdQuestions.size < createCount) {
            return distinctQuestions(createCount, create)
        }
        return createdQuestions
    }

    /**
     * 足し算の問題を生成して返す
     */
    private fun createPlusQuestions(createCount: Int): List<Question> {
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
            val value1Str = value1.toString()
            val onePlace = value1Str.substring(value1Str.length - 1, value1Str.length)
            val value2 = Random.nextInt(onePlace.toInt(), 10)

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
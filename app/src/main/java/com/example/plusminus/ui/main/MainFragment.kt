package com.example.plusminus.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plusminus.R
import com.example.plusminus.create_question.Lv2Math
import com.example.plusminus.databinding.FragmentMainBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.menuList) {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            val dividerItemDecoration = DividerItemDecoration(requireContext(), linearLayoutManager.orientation)
            ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
                dividerItemDecoration.setDrawable(it)
            }
            addItemDecoration(dividerItemDecoration)
            layoutManager = linearLayoutManager
            adapter = MenuAdapter(requireContext()).apply {
                onClickMenuListener = this@MainFragment
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickMenu(menu: Menu) {
        val questions = when (menu.no) {
            MenuNo.LV2_PLUS -> {
                distinctQuestions(CREATE_COUNT) { Lv2Math.createPlusQuestions(it) }
            }

            MenuNo.LV2_MINUS -> {
                distinctQuestions(CREATE_COUNT) { Lv2Math.createMinusQuestions(it) }
            }

            MenuNo.LV2_PLUS_MINUS_MIX -> {
                val plusQuestions = distinctQuestions(CREATE_COUNT / 2) { Lv2Math.createPlusQuestions(it) }
                val minusQuestions = distinctQuestions(CREATE_COUNT / 2) { Lv2Math.createMinusQuestions(it) }
                shuffleCombine(plusQuestions, minusQuestions)
            }

            MenuNo.LV2_MULTIPLICATION -> {
                distinctQuestions(CREATE_COUNT) { Lv2Math.createMultiplicationQuestions(it) }

            }

            MenuNo.LV2_DIVISION -> {
                distinctQuestions(CREATE_COUNT) { Lv2Math.createDivisionQuestions(it) }

            }
        }
        binding.questionView.startQuestion(questions)
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
}

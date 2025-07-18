package com.kunal.quizapp.presentation.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kunal.quizapp.R
import com.kunal.quizapp.common.IntentKeyConstants
import com.kunal.quizapp.common.Utils.getParcelableCompat
import com.kunal.quizapp.common.ViewExtensions.setBackgroundTint
import com.kunal.quizapp.databinding.FragmentQuestionsBinding
import com.kunal.quizapp.domain.models.Question
import com.kunal.quizapp.presentation.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuestionFragment : Fragment() {

    companion object {
        fun newInstance(
            question: Question,
            questionNumber: Int,
            totalQuestions: Int,
            onSkipClicked: (Int) -> Unit
        ): QuestionFragment {
            return QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IntentKeyConstants.QUESTION_ITEM, question)
                    putInt(IntentKeyConstants.QUESTION_NUMBER, questionNumber)
                    putInt(IntentKeyConstants.TOTAL_QUESTIONS, totalQuestions)
                }
                this.onSkipClicked = onSkipClicked
            }
        }
    }

    private val viewModel by activityViewModels<QuizViewModel>()

    private lateinit var binding: FragmentQuestionsBinding

    private val questionItem: Question by lazy {
        requireArguments().getParcelableCompat(IntentKeyConstants.QUESTION_ITEM)!!
    }

    private val questionNumber: Int by lazy {
        requireArguments().getInt(IntentKeyConstants.QUESTION_NUMBER)
    }

    private val totalQuestions: Int by lazy {
        requireArguments().getInt(IntentKeyConstants.TOTAL_QUESTIONS)
    }

    private var onSkipClicked: ((Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        tvQuestionProgress.text = getString(R.string.question_of, questionNumber + 1, totalQuestions)
        progressBar.progress = ((questionNumber + 1) * 100) / totalQuestions

        tvQuestionText.text = questionItem.question

        questionItem.options.forEachIndexed { index, option ->
            val optionButton = createOptionButton(option)
            optionButton.setOnClickListener {
                disableAllOptions()

                val isCorrect = index == questionItem.correctOptionIndex
                handleAnswerFeedback(optionButton, isCorrect, index)

                viewLifecycleOwner.lifecycleScope.launch {
                    delay(2000)
                    onSkipClicked?.invoke(questionNumber)
                }
            }

            optionsContainer.addView(optionButton)
        }

        btnSkip.setOnClickListener {
            onSkipClicked?.invoke(questionNumber)
        }
    }

    private fun createOptionButton(text: String): Button {
        return Button(requireContext()).apply {
            this.text = text
            id = View.generateViewId()
            setTextColor(Color.WHITE)
            textSize = 16f
            isAllCaps = false

            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.button_background_color)
            )

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 0)
            }
        }
    }

    private fun disableAllOptions() {
        for (i in 0 until binding.optionsContainer.childCount) {
            binding.optionsContainer.getChildAt(i).isEnabled = false
        }
    }

    private fun handleAnswerFeedback(selectedButton: Button, isCorrect: Boolean, selectedIndex: Int) {
        if (isCorrect) {
            selectedButton.setBackgroundTint(R.color.correct_answer_color)
            viewModel.onAnswer(true)
        } else {
            selectedButton.setBackgroundTint(R.color.wrong_answer_color)
            val correctButton = binding.optionsContainer
                .getChildAt(questionItem.correctOptionIndex) as Button
            correctButton.setBackgroundTint(R.color.correct_answer_color)
            viewModel.onAnswer(false)
        }
    }
}

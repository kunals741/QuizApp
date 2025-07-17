package com.kunal.quizapp.presentation.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kunal.quizapp.common.IntentKeyConstants
import com.kunal.quizapp.databinding.FragmentQuestionsBinding
import com.kunal.quizapp.domain.models.Question
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuestionFragment : Fragment() {

    companion object {
        fun newInstance(
            question: Question,
            questionNumber: Int,
            totalQuestions: Int,
            onSkipClicked: (Int) -> Unit
        ): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putParcelable(IntentKeyConstants.QUESTION_ITEM, question)
            args.putInt(IntentKeyConstants.QUESTION_NUMBER, questionNumber)
            args.putInt(IntentKeyConstants.TOTAL_QUESTIONS, totalQuestions)
            fragment.arguments = args
            fragment.onSkipClicked = onSkipClicked
            return fragment
        }
    }

    private val questionItem: Question by lazy { requireArguments().getParcelable(IntentKeyConstants.QUESTION_ITEM)!! } //todo deprecated
    private val questionNumber: Int by lazy { requireArguments().getInt(IntentKeyConstants.QUESTION_NUMBER) }
    private val totalQuestions: Int by lazy { requireArguments().getInt(IntentKeyConstants.TOTAL_QUESTIONS) }
    private lateinit var binding: FragmentQuestionsBinding

    private var onSkipClicked :( (Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvQuestionProgress.text = "Question ${questionNumber + 1} of $totalQuestions" //todo
            progressBar.progress = ((questionNumber + 1) * 100) / totalQuestions

            binding.tvQuestionText.text = questionItem.question
            questionItem.options.forEachIndexed { index, option ->
                val button = Button(requireContext()).apply {
                    text = option
                    id = View.generateViewId()
                    setTextColor(Color.WHITE)
                    textSize = 16f
                    backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2D2D2D"))
                    isAllCaps = false
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 16, 0, 0)
                    }

                    setOnClickListener {
                        for (i in 0 until optionsContainer.childCount) {
                            optionsContainer.getChildAt(i).isEnabled = false
                        }
                        if (index == questionItem.correctOptionIndex) {
                            backgroundTintList =
                                ColorStateList.valueOf(Color.parseColor("#4CAF50")) // green
                        } else {
                            backgroundTintList =
                                ColorStateList.valueOf(Color.parseColor("#F44336")) // red
                            val correctButton =
                                optionsContainer.getChildAt(questionItem.correctOptionIndex) as Button
                            correctButton.backgroundTintList =
                                ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                        }

                        viewLifecycleOwner.lifecycleScope.launch {
                            delay(2000)
                            onSkipClicked?.invoke(questionNumber)
                        }
                    }
                }
                optionsContainer.addView(button)
            }
            btnSkip.setOnClickListener {
                onSkipClicked?.invoke(questionNumber)
            }
        }
    }
}

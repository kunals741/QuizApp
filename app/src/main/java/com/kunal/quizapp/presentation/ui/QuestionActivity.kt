package com.kunal.quizapp.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kunal.quizapp.common.IntentKeyConstants
import com.kunal.quizapp.common.Utils.getParcelableArrayListCompat
import com.kunal.quizapp.databinding.ActivityQuestionBinding
import com.kunal.quizapp.domain.models.Question
import com.kunal.quizapp.presentation.adapter.QuestionPagerAdapter
import com.kunal.quizapp.presentation.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionActivity : AppCompatActivity() {

    companion object {
        fun launchQuestionActivity(activity: Activity, questionsList: ArrayList<Question>) {
            val intent = Intent(activity, QuestionActivity::class.java).apply {
                putParcelableArrayListExtra(IntentKeyConstants.QUESTIONS_DATA, questionsList)
            }
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityQuestionBinding
    private val quizViewModel by viewModels<QuizViewModel>()

    private val questionsList: ArrayList<Question> by lazy {
        intent.getParcelableArrayListCompat(IntentKeyConstants.QUESTIONS_DATA)!!
    }

    private val scoreBoardLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.viewPager.currentItem = 0
            quizViewModel.reset()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (quizViewModel.questionsList.isEmpty()) {
            quizViewModel.questionsList = questionsList
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = QuestionPagerAdapter(this, questionsList) { currentPosition ->
            if (currentPosition < questionsList.lastIndex) {
                binding.viewPager.currentItem = currentPosition + 1
            } else {
                val intent = ScoreBoardActivity.getScoreBoardActivityIntent(
                    activity = this,
                    totalCorrectAnswers = quizViewModel.totalCorrectAnswers,
                    highestStreak = quizViewModel.highestStreak,
                    totalQuestions = questionsList.size
                )
                scoreBoardLauncher.launch(intent)
            }
        }

        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
    }
}

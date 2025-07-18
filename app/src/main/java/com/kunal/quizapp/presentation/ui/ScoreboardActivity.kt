package com.kunal.quizapp.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.kunal.quizapp.R
import com.kunal.quizapp.common.IntentKeyConstants
import com.kunal.quizapp.databinding.ActivityScoreboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreboardBinding

    private val totalCorrectAnswers: Int by lazy {
        intent.getIntExtra(IntentKeyConstants.CORRECT_ANSWERS, 0)
    }

    private val highestStreak: Int by lazy {
        intent.getIntExtra(IntentKeyConstants.HIGHEST_STREAK, 0)
    }

    private val totalQuestions: Int by lazy {
        intent.getIntExtra(IntentKeyConstants.TOTAL_QUESTIONS, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCorrectValue.text = getString(
            R.string.correct_answers_value,
            totalCorrectAnswers,
            totalQuestions
        )

        binding.tvStreakValue.text = highestStreak.toString()

        binding.btnRestartQuiz.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            //empty to disable back
            override fun handleOnBackPressed() {}
        })
    }

    companion object {
        fun getScoreBoardActivityIntent(
            activity: Activity,
            totalCorrectAnswers: Int,
            highestStreak: Int,
            totalQuestions: Int
        ): Intent {
            return Intent(activity, ScoreBoardActivity::class.java).apply {
                putExtra(IntentKeyConstants.CORRECT_ANSWERS, totalCorrectAnswers)
                putExtra(IntentKeyConstants.HIGHEST_STREAK, highestStreak)
                putExtra(IntentKeyConstants.TOTAL_QUESTIONS, totalQuestions)
            }
        }
    }
}

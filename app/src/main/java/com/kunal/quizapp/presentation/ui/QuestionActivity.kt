package com.kunal.quizapp.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

class QuestionActivity : AppCompatActivity() {

    companion object {
        fun launchQuestionActivity(
            activity: Activity,
            questionsList: ArrayList<Question>
        ) {
            val intent = Intent(activity, QuestionActivity::class.java).apply {
                putParcelableArrayListExtra(
                    IntentKeyConstants.QUESTIONS_DATA,
                    ArrayList(questionsList)
                )
            }
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityQuestionBinding
    private val quizViewModel by viewModels<QuizViewModel>()

    private val questionsList: ArrayList<Question> by lazy {
        //using !! as this activity can be launched with questions data only
        intent.getParcelableArrayListCompat(IntentKeyConstants.QUESTIONS_DATA)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (quizViewModel.questionsList.isEmpty()) {
            quizViewModel.questionsList = questionsList
        }

        val adapter = QuestionPagerAdapter(this, questionsList){ currentPosition ->
            if (currentPosition < questionsList.size - 1) {
                binding.viewPager.currentItem = currentPosition + 1
            } else {
                //todo scoreboard screen
                Toast.makeText(this, "End of quiz", Toast.LENGTH_SHORT).show()
            }
        }

        binding.viewPager.adapter = adapter
    }
}
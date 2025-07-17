package com.kunal.quizapp.presentation.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunal.quizapp.R
import com.kunal.quizapp.common.Utils
import com.kunal.quizapp.domain.models.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.launch {
            val questions = loadQuizData(this@SplashScreenActivity)
            if (questions.isEmpty()) {
                showErrorDialog()
            } else {
                // proceed with normal flow, e.g., navigate to next screen
                Toast.makeText(this@SplashScreenActivity, "Quiz data loaded successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun loadQuizData(context: Context): List<Question> {
        return withContext(Dispatchers.IO) {
            try {
                val questionType = object : TypeToken<List<Question>>() {}.type
                Gson().fromJson<List<Question>>(
                    Utils.getJsonFromAssets(context, "question_and_answers.json"),
                    questionType
                )
            } catch (e: Exception) {
                Log.e("SplashScreenActivity", "Error loading questions", e)
                emptyList()
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error))
            .setMessage(getString(R.string.failed_to_load_quiz_data_please_try_again_later))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }
}
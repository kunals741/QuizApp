package com.kunal.quizapp.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunal.quizapp.common.Utils
import com.kunal.quizapp.domain.QuizRepository
import com.kunal.quizapp.domain.models.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor() : QuizRepository {

    override suspend fun loadQuizData(context: Context): List<Question> {
        return withContext(Dispatchers.IO) {
            try {
                val questionType = object : TypeToken<List<Question>>() {}.type
                Gson().fromJson<List<Question>>(
                    Utils.getJsonFromAssets(context, "question_and_answers.json"),
                    questionType
                )
            } catch (e: Exception) {
                Log.e("QuizApp", "Error loading questions", e)
                emptyList()
            }
        }
    }
}
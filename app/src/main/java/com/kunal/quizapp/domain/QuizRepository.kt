package com.kunal.quizapp.domain

import android.content.Context
import com.kunal.quizapp.domain.models.Question

interface QuizRepository {
    suspend fun loadQuizData(context: Context): List<Question>
}
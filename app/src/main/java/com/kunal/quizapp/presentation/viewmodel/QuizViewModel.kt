package com.kunal.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.kunal.quizapp.domain.models.Question

class QuizViewModel : ViewModel() {

    var questionsList : List<Question> = emptyList()
}
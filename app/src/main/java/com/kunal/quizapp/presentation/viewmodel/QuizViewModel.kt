package com.kunal.quizapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunal.quizapp.domain.QuizRepository
import com.kunal.quizapp.domain.models.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {
    var questionsList: List<Question> = emptyList()

    private val _streakCountLiveData = MutableLiveData(0)
    val streakCountLiveData: LiveData<Int> get() = _streakCountLiveData

    private val _quizData = MutableLiveData<List<Question>>()
    val quizData: LiveData<List<Question>> = _quizData


    var highestStreak: Int = 0
        private set

    var totalCorrectAnswers: Int = 0
        private set

    fun loadQuizData(context: Context) {
        viewModelScope.launch {
            try {
                val questions = quizRepository.loadQuizData(context)
                delay(2000) //delaying to display splash screen
                _quizData.value = questions
            } catch (e: Exception) {
                _quizData.value = emptyList()
            }
        }
    }

    fun onAnswer(correct: Boolean) {
        if (correct) {
            val currentStreak = (_streakCountLiveData.value ?: 0) + 1
            _streakCountLiveData.value = currentStreak

            totalCorrectAnswers += 1

            if (currentStreak > highestStreak) {
                highestStreak = currentStreak
            }
        } else {
            _streakCountLiveData.value = 0
        }
    }

    fun reset() {
        _streakCountLiveData.value = 0
        highestStreak = 0
        totalCorrectAnswers = 0
    }
}
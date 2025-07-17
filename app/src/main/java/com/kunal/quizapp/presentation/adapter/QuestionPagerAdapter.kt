package com.kunal.quizapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kunal.quizapp.domain.models.Question
import com.kunal.quizapp.presentation.ui.QuestionFragment

class QuestionPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val questions: List<Question>,
    private val onSkipClicked: (Int) -> Unit
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(questions[position], position, itemCount){
            onSkipClicked(it)
        }
    }
}

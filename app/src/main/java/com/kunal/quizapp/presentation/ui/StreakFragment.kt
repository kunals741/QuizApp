package com.kunal.quizapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kunal.quizapp.R
import com.kunal.quizapp.databinding.FragmentStreakBinding
import com.kunal.quizapp.presentation.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StreakFragment : Fragment() {

    private lateinit var binding: FragmentStreakBinding

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.streakCountLiveData.observe(viewLifecycleOwner) { streak ->
            updateStreakUI(streak)
        }
    }

    private fun updateStreakUI(streak: Int) = with(binding) {
        val fireIcons = listOf(fire1, fire2, fire3)
        val fireDrawable = getFireDrawable(streak)

        fireIcons.forEachIndexed { index, imageView ->
            imageView.setImageResource(fireDrawable[index])
        }

        streakCount.text = streak.toString()
    }

    private fun getFireDrawable(streak: Int): List<Int> {
        val inactive = R.drawable.ic_fire_outline
        val medium = R.drawable.ic_fire_medium
        val active = R.drawable.ic_fire_active

        return when (streak) {
            0 -> listOf(inactive, inactive, inactive)
            1 -> listOf(medium, inactive, inactive)
            2 -> listOf(medium, medium, inactive)
            else -> listOf(active, active, active)
        }
    }
}

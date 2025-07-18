package com.kunal.quizapp.common

import android.content.res.ColorStateList
import android.widget.Button
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

object ViewExtensions {
    fun Button.setBackgroundTint(@ColorRes colorRes: Int) {
        backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context, colorRes)
        )
    }
}
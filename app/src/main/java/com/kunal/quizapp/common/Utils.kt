package com.kunal.quizapp.common

import android.content.Context

object Utils {
    fun getJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
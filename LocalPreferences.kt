package com.traceless.privacy.data

import android.content.Context

class LocalPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("traceless_local", Context.MODE_PRIVATE)

    fun isChecked(id: String): Boolean = prefs.getBoolean(id, false)

    fun setChecked(id: String, checked: Boolean) {
        prefs.edit().putBoolean(id, checked).apply()
    }

    fun saveLastReview(timestamp: Long) {
        prefs.edit().putLong("last_review", timestamp).apply()
    }

    fun lastReview(): Long = prefs.getLong("last_review", 0L)
}

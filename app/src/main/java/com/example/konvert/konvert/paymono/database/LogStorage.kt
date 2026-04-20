package com.example.konvert.konvert.paymono.database

import android.content.Context
import android.content.SharedPreferences

object LogStorage {
    private const val PREFS_NAME = "AppLogs"
    private const val LOG_KEY = "logs"
    private const val MAX_LOGS = 100

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun addLog(context: Context, message: String) {
        val prefs = getPrefs(context)
        val logs = getLogs(context).toMutableList()

        logs.add(message)
        if (logs.size > MAX_LOGS) {
            logs.removeAt(0) // Видаляємо найстаріший запис
        }

        prefs.edit().putString(LOG_KEY, logs.joinToString("\n")).apply()
    }

    fun getLogs(context: Context): List<String> {
        val prefs = getPrefs(context)
        return prefs.getString(LOG_KEY, "")?.split("\n") ?: emptyList()
    }

    fun clearLogs(context: Context) {
        getPrefs(context).edit().remove(LOG_KEY).apply()
    }
}

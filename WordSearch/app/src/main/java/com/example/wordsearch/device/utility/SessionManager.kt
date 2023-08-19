package com.example.wordsearch.device.utility

import android.content.SharedPreferences

class SessionManager() {

    private val sharedPreferences: SharedPreferences?
    private val editor: SharedPreferences.Editor
    private val PRIVATE_MODE = 0

    fun logoutUser() {
        editor.clear()
        editor.commit()
    }

    var keyAlphabets: String?
        get() = sharedPreferences!!.getString(KEY_ALPHABETS, "")
        set(string) {
            editor.putString(KEY_ALPHABETS, string)
            editor.commit()
        }

    var keyRow: String?
        get() = sharedPreferences!!.getString(KEY_ROW, "")
        set(row) {
            editor.putString(KEY_ROW, row)
            editor.commit()
        }

    var keyColumn: String?
        get() = sharedPreferences!!.getString(KEY_COLUMN, "")
        set(column) {
            editor.putString(KEY_COLUMN, column)
            editor.commit()
        }

    companion object {
        private const val PREF_NAME = "WordPref"
        private const val KEY_ALPHABETS = "KEY_ALPHABETS"
        private const val KEY_ROW = "KEY_ROW"
        private const val KEY_COLUMN = "KEY_COLUMN"
    }

    init {
        sharedPreferences = MyApp.appContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = sharedPreferences.edit()
    }

}

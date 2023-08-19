package com.example.wordsearch.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wordsearch.R
import com.example.wordsearch.device.utility.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            startActivity(
                Intent(
                    this@SplashActivity,
                    if (SessionManager().keyAlphabets?.isEmpty() == true)
                        MainActivity::class.java else GridWordActivity::class.java
                )
            )
            finish()
        }
    }
}
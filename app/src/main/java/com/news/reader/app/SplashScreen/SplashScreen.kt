package com.news.reader.app.SplashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.news.reader.app.R
import com.news.reader.app.activity.NewsMainActivity

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        val handler = android.os.Handler()
        handler.postDelayed({
            val intent = Intent(this, NewsMainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}
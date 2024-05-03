package com.news.reader.app.SplashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.news.reader.app.R
import com.news.reader.app.activity.NewsMainActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        val handler = android.os.Handler()
        handler.postDelayed({
            val intent = Intent(this, NewsMainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)

    }
}
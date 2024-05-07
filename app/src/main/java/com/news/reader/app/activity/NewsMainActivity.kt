package com.news.reader.app.activity


import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.news.reader.app.R
import com.news.reader.app.databinding.NewsMainActivityBinding

@RequiresApi(Build.VERSION_CODES.M)
class NewsMainActivity : AppCompatActivity() {

    private lateinit var binding: NewsMainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = NewsMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //enableEdgeToEdge()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }
}
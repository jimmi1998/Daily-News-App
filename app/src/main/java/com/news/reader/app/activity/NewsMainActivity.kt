package com.news.reader.app.activity


import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.news.reader.app.R
import com.news.reader.app.adapters.NewsArticleAdapter
import com.news.reader.app.databinding.NewsMainActivityBinding
import com.news.reader.app.model.NewsArticle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.M)
class NewsMainActivity : AppCompatActivity() {

    private lateinit var binding: NewsMainActivityBinding
    private lateinit var newsArticleAdapter: NewsArticleAdapter
    private var articleList = mutableListOf<NewsArticle>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = NewsMainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }

    fun sortByDate(ascending: Boolean) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        if (ascending) {
            articleList.sortWith(compareBy {
                try {
                    dateFormat.parse(it.publishedDate ?: "")
                } catch (e: Exception) {
                    Date(0)
                }
            })
        } else {
            articleList.sortWith(compareByDescending {
                try {
                    dateFormat.parse(it.publishedDate ?: "")
                } catch (e: Exception) {
                    Date(0)
                }
            })
        }
        newsArticleAdapter.notifyDataSetChanged()
    }

}
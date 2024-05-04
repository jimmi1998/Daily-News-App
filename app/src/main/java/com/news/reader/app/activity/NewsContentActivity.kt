package com.news.reader.app.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.news.reader.app.R
import com.news.reader.app.model.NewsArticle
import com.news.reader.app.model.Source
import com.news.reader.app.utils.SavedNewsUtils

class NewsContentActivity : AppCompatActivity() {

    private lateinit var articleURL: String
    private lateinit var articleTitle: String
    private lateinit var articleContent: String
    private lateinit var articleAuthor: String
    private lateinit var imageUrl: String
    private lateinit var articleSource: String
    private lateinit var articlePublishDate: String
    private lateinit var newsArticle: NewsArticle

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_content)

        articleURL = intent.getStringExtra("ARTICLE_URL").toString()
        articleContent = intent.getStringExtra("ARTICLE_CONTENT").toString()
        articleTitle = intent.getStringExtra("ARTICLE_TITLE").toString()
        articleAuthor = intent.getStringExtra("ARTICLE_AUTHOR").toString()
        articleSource = intent.getStringExtra("ARTICLE_SOURCE").toString()
        imageUrl = intent.getStringExtra("ARTICLE_IMAGE").toString()
        articlePublishDate = intent.getStringExtra("ARTICLE_PUBLISH_DATE").toString()
        newsArticle = intent.getParcelableExtra("ARTICLE_DATA", NewsArticle::class.java)!!

        val contentText = findViewById<TextView>(R.id.content)
        val titleText = findViewById<TextView>(R.id.title)
        val authorText = findViewById<TextView>(R.id.author)
        val sourceText = findViewById<TextView>(R.id.source)
        val publishedDate = findViewById<TextView>(R.id.publishedDate)
        val image = findViewById<ImageView>(R.id.news_Image)
        val readMoreButton = findViewById<TextView>(R.id.readMoreBtn)
        val shareNews = findViewById<ImageView>(R.id.shareNews)

        contentText.text = articleContent
        titleText.text = newsArticle.title
        authorText.text = getString(R.string.written_by, articleAuthor)
        sourceText.text = getString(R.string.source_concat, articleSource)
        publishedDate.text = getString(R.string.date, articlePublishDate)

        val savedButton = findViewById<ImageView>(R.id.bookmark)
        val newsArticle = createArticleObject()

        // Set the appropriate image based on the article's save status
        if (SavedNewsUtils.isArticleSaved(this, newsArticle)) {
            // Load the saved icon image
            savedButton.setImageResource(R.drawable.bookmarked)

        } else {
            // Load the unsaved icon image
            savedButton.setImageResource(R.drawable.not_bookmark)
        }

        savedButton.setOnClickListener {
        // Saved the news
            val savedNewsArticle = createArticleObject()
            if (SavedNewsUtils.isArticleSaved(this, savedNewsArticle)) {
                SavedNewsUtils.unSaveArticle(this, savedNewsArticle)
                savedButton.setImageResource(R.drawable.not_bookmark)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.unsave_article),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                SavedNewsUtils.saveArticle(this, savedNewsArticle)
                savedButton.setImageResource(R.drawable.bookmarked)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.watch_later),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        imageUrl.let {
            Glide.with(this)
                .load(it)
                .apply(
                    RequestOptions()
                        .error(R.drawable.news_dummy_image)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false // Return false to allow Glide to handle the failure event
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        }

        // Set a click listener for the read more button to open Article URL in Browser
        readMoreButton.setOnClickListener {
            val intent = Intent(this, NewsContentActivity::class.java)
            intent.putExtra("ARTICLE_URL", articleURL)
            openUrlInBrowser(articleURL)
        }

        shareNews.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, articleURL)
            startActivity(Intent.createChooser(shareIntent, "Share News"))
        }
    }

    private fun openUrlInBrowser(articleURL: String) {
        val articleOpenUrl = Uri.parse(articleURL)
        val openBrowserIntent = Intent(Intent.ACTION_VIEW, articleOpenUrl)

        startActivity(openBrowserIntent)
    }

    private fun createArticleObject(): NewsArticle {
        // Create a Source object
        val sourceObject = Source(newsArticle.source?.id, newsArticle.source?.name)

        // Assuming you have these values available in your context
        val title = newsArticle.title
        val author = newsArticle.author
        val content = newsArticle.content
        val imageUrl = newsArticle.imageUrl
        val url = newsArticle.url
        val des = newsArticle.description
        val publishedDate = newsArticle.publishedDate

        // Created an Article object using the gathered data
        val newsArticleObject = NewsArticle (
            sourceObject,
            title,
            des,
            imageUrl,
            author,
            publishedDate,
            content,
            url
        )
        // Returning the Article object created using the gathered data
        return newsArticleObject
    }
}
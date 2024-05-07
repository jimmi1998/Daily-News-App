package com.news.reader.app.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.news.reader.app.R
import com.news.reader.app.activity.NewsContentActivity
import com.news.reader.app.model.NewsArticle
import com.news.reader.app.utils.AppUtils

class NewsArticleAdapter(
    private val context: Context,
    private val newsArticleList: List<NewsArticle>,
    private val onArticleClickListener: OnArticleClickListener
) : RecyclerView.Adapter<NewsArticleAdapter.NewsArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_article, parent, false)
        return NewsArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val newsArticle = newsArticleList[position]

        holder.bind(newsArticle, onArticleClickListener)

        holder.itemView.setOnClickListener {
            Log.d("URL BODY", "${newsArticle.url}")

            val intent = Intent(context, NewsContentActivity::class.java)

            intent.putExtra("ARTICLE_URL", newsArticle.url)
            intent.putExtra("ARTICLE_IMAGE", newsArticle.imageUrl)
            intent.putExtra("ARTICLE_CONTENT", newsArticle.content)
            intent.putExtra("ARTICLE_AUTHOR", newsArticle.author)
            intent.putExtra("ARTICLE_TITLE", newsArticle.title)
            intent.putExtra("ARTICLE_SOURCE", newsArticle.source?.name)
            intent.putExtra("ARTICLE_PUBLISH_DATE", newsArticle.publishedDate)
            intent.putExtra("ARTICLE_DATA", newsArticle)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return newsArticleList.size
    }

    inner class NewsArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        private val description: TextView = itemView.findViewById(R.id.desc)
        private val author: TextView = itemView.findViewById(R.id.author)
        private val source: TextView = itemView.findViewById(R.id.source)
        private val publishDate: TextView = itemView.findViewById(R.id.publishDate)

        private val newsImage: ImageView = itemView.findViewById(R.id.newsImage)

        fun bind(newsArticle: NewsArticle, clickListener: OnArticleClickListener) {

            newsTitle.text = newsArticle.title ?: context.getString(R.string.title)
            description.text = newsArticle.description ?: context.getString(R.string.new_article_short_description)
            author.text = newsArticle.author ?: context.getString(R.string.author)
            source.text = newsArticle.source?.name ?: context.getString(R.string.source)
            publishDate.text = AppUtils.formatDate(newsArticle.publishedDate)

            newsArticle.imageUrl?.let {
                Glide.with(context)
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
                    .into(newsImage)
            } ?: run {
                newsImage.setImageResource(R.drawable.news_dummy_image)
            }

            itemView.setOnClickListener {
                clickListener.onArticleClick(newsArticle)
            }

        }

    }

    interface OnArticleClickListener {
        fun onArticleClick(newsArticle: NewsArticle)
    }
}

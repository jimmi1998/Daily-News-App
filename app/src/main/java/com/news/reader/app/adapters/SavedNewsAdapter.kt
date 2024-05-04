package com.news.reader.app.adapters

import android.content.Context
import android.graphics.drawable.Drawable
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
import com.news.reader.app.model.NewsArticle
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class SavedNewsAdapter(
    private val newsArticleList: List<NewsArticle>,
    private val context: Context,
    private val clickListener: OnArticleClickListener // Add the listener in the constructor
) : RecyclerView.Adapter<SavedNewsAdapter.NewsArticleViewHolder>() {

    inner class NewsArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTitle: TextView = itemView.findViewById(R.id.newsTitle)
        val description: TextView = itemView.findViewById(R.id.desc)
        val author: TextView = itemView.findViewById(R.id.author)
        val source: TextView = itemView.findViewById(R.id.source)
        val publishDate: TextView = itemView.findViewById(R.id.publishDate)
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_article, parent, false)
        return NewsArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val newsArticle = newsArticleList[position]

        holder.newsTitle.text = newsArticle.title ?: context.getString(R.string.title)
        holder.source.text = newsArticle.source?.name ?: context.getString(R.string.source)
        holder.author.text = newsArticle.author ?: context.getString(R.string.author)
        holder.description.text = newsArticle.description ?: context.getString(R.string.new_article_short_description)
        holder.publishDate.text = formatDate(newsArticle.publishedDate)

        newsArticle.imageUrl?.let {
            Glide.with(context)
                .load(it)
                .apply(RequestOptions().error(R.drawable.news_dummy_image))
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
                .into(holder.newsImage)
        } ?: run {
            holder.newsImage.setImageResource(R.drawable.news_dummy_image)
        }

        holder.itemView.setOnClickListener {
            // Handle click event if needed
            clickListener.onArticleClick(newsArticle)
        }

    }

    override fun getItemCount(): Int {
        return newsArticleList.size
    }

    private fun formatDate(dateString: String?): String {
        return if (dateString.isNullOrEmpty()) {
            "No date available" // or any other default value you prefer
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val date = inputFormat.parse(dateString)
                date?.let {
                    outputFormat.format(date)
                } ?: ""
            } catch (e: ParseException) {
                "Invalid date format" // Handle parsing exceptions
            }
        }
    }

    interface OnArticleClickListener {
        fun onArticleClick(newsArticle: NewsArticle)
    }


}
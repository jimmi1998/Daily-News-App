package com.news.reader.app.utils

import android.content.Context
import com.news.reader.app.model.NewsArticle
import com.news.reader.app.model.Source
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object SavedNewsUtils {
    private const val SHARED_PREFS_NAME = "SavedNewsPrefs"
    private const val KEY_SAVED_ARTICLES = "saved_articles"

    fun saveArticle(context: Context, newsArticle: NewsArticle) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedArticles = getSavedArticlesList(context).toMutableList()

        if (!savedArticles.contains(newsArticle)) {
            savedArticles.add(newsArticle)

            val editor = sharedPreferences.edit()
            val jsonArray = JSONArray()
            for (i in savedArticles.indices) {
                val jsonObject = JSONObject()
                val currentArticle = savedArticles[i]
                jsonObject.put("source_id", currentArticle.source?.id)
                jsonObject.put("source_name", currentArticle.source?.name)
                jsonObject.put("author", currentArticle.author)
                jsonObject.put("title", currentArticle.title)
                jsonObject.put("description", currentArticle.description)
                jsonObject.put("url", currentArticle.url)
                jsonObject.put("imageUrl", currentArticle.imageUrl)
                jsonObject.put("publishedAt", currentArticle.publishedDate)
                jsonObject.put("content", currentArticle.content)
                jsonArray.put(jsonObject)
            }
            editor.putString(KEY_SAVED_ARTICLES, jsonArray.toString())
            editor.apply()
        }
    }

    fun getSavedArticlesList(context: Context): List<NewsArticle> {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedArticles = mutableListOf<NewsArticle>()
        val jsonString = sharedPreferences.getString(KEY_SAVED_ARTICLES, null)

        if (!jsonString.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val sourceId = jsonObject.getString("source_id")
                    val sourceName = jsonObject.getString("source_name")
                    val author = jsonObject.getString("author")
                    val title = jsonObject.getString("title")
                    val description = jsonObject.getString("description")
                    val url = jsonObject.getString("url")
                    val imageUrl = jsonObject.getString("imageUrl")
                    val publishedAt = jsonObject.getString("publishedAt")
                    val content = jsonObject.getString("content")

                    val source = Source(sourceId, sourceName)
                    val newsArticle = NewsArticle(
                        source,
                        author,
                        title,
                        description,
                        url,
                        imageUrl,
                        publishedAt,
                        content
                    )
                    savedArticles.add(newsArticle)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return savedArticles
    }


    fun unSaveArticle(context: Context, newsArticle: NewsArticle) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val savedArticles = getSavedArticlesList(context).toMutableList()

        val iterator = savedArticles.iterator()
        while (iterator.hasNext()) {
            val savedArticle = iterator.next()
            if (savedArticle == newsArticle) {
                iterator.remove()
                break // Assuming articles are unique and there's no need to continue iterating
            }
        }

        val editor = sharedPreferences.edit()
        val jsonArray = JSONArray()
        for (i in savedArticles.indices) {
            val jsonObject = JSONObject()
            val currentArticle = savedArticles[i]
            jsonObject.put("source_id", currentArticle.source?.id)
            jsonObject.put("source_name", currentArticle.source?.name)
            jsonObject.put("title", currentArticle.title)
            jsonObject.put("description", currentArticle.description)
            jsonObject.put("imageUrl", currentArticle.imageUrl)
            jsonObject.put("author", currentArticle.author)
            jsonObject.put("publishedAt", currentArticle.publishedDate)
            jsonObject.put("content", currentArticle.content)
            jsonObject.put("url", currentArticle.url)

            jsonArray.put(jsonObject)
        }
        editor.putString(KEY_SAVED_ARTICLES, jsonArray.toString())
        editor.apply()
    }

    fun isArticleSaved(context: Context, newsArticle: NewsArticle): Boolean {
        val savedArticles = getSavedArticlesList(context)
        return savedArticles.contains(newsArticle)
    }


    fun isEmpty(context: Context): Boolean {
        val savedArticles = getSavedArticlesList(context)
        return savedArticles.isEmpty()
    }


}

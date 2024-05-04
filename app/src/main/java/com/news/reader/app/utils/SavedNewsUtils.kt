package com.news.reader.app.utils

import android.content.Context
import com.news.reader.app.model.NewsArticle
import com.news.reader.app.model.Source
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object SavedNewsUtils {
    private const val SHAREDPREFSNAME = AppConstants.SHARED_PREFS_NAME
    private const val KEYSAVEDARTICLES = AppConstants.KEY_SAVED_ARTICLES

    fun saveArticle(context: Context, newsArticle: NewsArticle) {
        val sharedPreferences =
            context.getSharedPreferences(SHAREDPREFSNAME, Context.MODE_PRIVATE)
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
            editor.putString(KEYSAVEDARTICLES, jsonArray.toString())
            editor.apply()
        }
    }

    fun getSavedArticlesList(context: Context): List<NewsArticle> {
        val sharedPreferences =
            context.getSharedPreferences(SHAREDPREFSNAME, Context.MODE_PRIVATE)
        val savedArticlesList = mutableListOf<NewsArticle>()
        val jsonString = sharedPreferences.getString(KEYSAVEDARTICLES, null)

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
                    val publishedDate = jsonObject.getString("publishedAt")
                    val content = jsonObject.getString("content")

                    val source = Source(sourceId, sourceName)
                    val newsArticle = NewsArticle(
                        source,
                        title,
                        description,
                        imageUrl,
                        author,
                        publishedDate,
                        content,
                        url
                    )
                    savedArticlesList.add(newsArticle)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return savedArticlesList
    }


    fun unSaveArticle(context: Context, newsArticle: NewsArticle) {
        val sharedPreferences =
            context.getSharedPreferences(SHAREDPREFSNAME, Context.MODE_PRIVATE)
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
        editor.putString(KEYSAVEDARTICLES, jsonArray.toString())
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

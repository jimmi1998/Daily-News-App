package com.news.reader.app.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.news.reader.app.R
import com.news.reader.app.adapters.NewsArticleAdapter
import com.news.reader.app.model.NewsArticle
import com.news.reader.app.model.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.news.reader.app.utils.AppConstants

class NewsList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsArticleAdapter: NewsArticleAdapter
    private var newsArticlesList = mutableListOf<NewsArticle>()
    private lateinit var filter: View
    private lateinit var progressBar: ProgressBar
    private lateinit var sortBy: Array<String>
    private var selectedPosition = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.news_list_fragment, container, false)

        recyclerView = view.findViewById(R.id.news_list_recycler_View)
        setupRecyclerView(view)
        fetchArticles()

        progressBar = view.findViewById(R.id.main_progress_bar)
        progressBar.visibility = View.VISIBLE

        filter = view.findViewById(R.id.filter)
        filter.setOnClickListener {
            showFilterDialog()
        }
        return view
    }

    private fun setupRecyclerView(view: View) {
        newsArticleAdapter = NewsArticleAdapter(
            requireContext(),
            newsArticlesList,
            object : NewsArticleAdapter.OnArticleClickListener {
                override fun onArticleClick(newsArticle: NewsArticle) {
                }
            })
        recyclerView = view.findViewById<RecyclerView>(R.id.news_list_recycler_View).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsArticleAdapter
        }
    }
    private fun fetchArticles() {
        val apiUrl = AppConstants.BASE_URL

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = URL(apiUrl).readText()
                val articles = parseArticles(response)
                withContext(Dispatchers.Main) {
                    newsArticlesList.clear()
                    newsArticlesList.addAll(articles)
                    newsArticleAdapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    // Show a system-generated dialog for network error
                    showSystemDialog(getString(R.string.network_error),
                        getString(R.string.please_check_your_internet_connection))
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle other exceptions if required
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun parseArticles(response: String): List<NewsArticle> {
        val newsArticlesList = mutableListOf<NewsArticle>()
        try {
            val jsonResponse = JSONObject(response)
            val jsonArticles = jsonResponse.optJSONArray("articles")

            jsonArticles?.let {
                for (i in 0 until it.length()) {
                    val jsonArticle = it.getJSONObject(i)
                    val sourceJson = jsonArticle.optJSONObject("source")
                    val source = Source(sourceJson?.optString("id"), sourceJson?.optString("name"))

                    val newsArticle = NewsArticle(
                        source,
                        jsonArticle.optString("title"),
                        jsonArticle.optString("description"),
                        jsonArticle.optString("urlToImage"),
                        jsonArticle.optString("author"),
                        jsonArticle.optString("publishedAt"),
                        jsonArticle.optString("content"),
                        jsonArticle.optString("url"),
                    )
                    newsArticlesList.add(newsArticle)
                }
            }
        } catch (e: JSONException) {
            Log.e("PARSE_ERROR", "JSONException: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("PARSE_ERROR", "Exception occurred while parsing JSON: ${e.message}", e)
        }
        return newsArticlesList
    }

    private fun sortByDate(ascending: Boolean) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        if (ascending) {
            newsArticlesList.sortWith(compareBy {
                try {
                    dateFormat.parse(it.publishedDate ?: "")
                } catch (e: Exception) {
                    Date(0)
                }
            })
        } else {
            newsArticlesList.sortWith(compareByDescending {
                try {
                    dateFormat.parse(it.publishedDate ?: "")
                } catch (e: Exception) {
                    Date(0)
                }
            })
        }
        newsArticleAdapter.notifyDataSetChanged()
    }

    private fun showFilterDialog() {
        filter.visibility = View.GONE

        sortBy = resources.getStringArray(R.array.sort_by)
        var selectedItem = sortBy[selectedPosition]

        AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            .setTitle(R.string.sort_by)
            .setSingleChoiceItems(sortBy, selectedPosition) { _, which ->
                selectedPosition = which
                selectedItem = sortBy[which]
            }
            .setPositiveButton(R.string.ok) { _, _ ->
                when(selectedPosition) {
                    0 -> sortByDate(false)
                    1 -> sortByDate(true)
                    else -> {
                        sortByDate(false)
                        selectedItem = sortBy[0]
                    }
                }
                showSnackBar(selectedItem)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                filter.visibility = View.VISIBLE
            }
            .setCancelable(false)
            .show()
    }

    private fun showSnackBar(msg: String){
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showSystemDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}
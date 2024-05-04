package com.news.reader.app.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.news.reader.app.R
import com.news.reader.app.activity.NewsContentActivity
import com.news.reader.app.adapters.SavedNewsAdapter
import com.news.reader.app.model.NewsArticle
import com.news.reader.app.utils.AppConstants
import com.news.reader.app.utils.SavedNewsUtils

class SavedNews : Fragment() {
    private lateinit var savedArticlesList: MutableList<NewsArticle>
    private lateinit var recyclerView: RecyclerView
    private lateinit var savedNewsAdapter: SavedNewsAdapter

    private val requestCodeRefresh = AppConstants.REQUEST_CODE_REFRESH // Choose your request code


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.saved_news_fragment, container, false)

        setSavedNews(view)

        return view
    }

    private fun setSavedNews(view : View?) {
        val nothingFoundAnimation = view?.findViewById<LottieAnimationView>(R.id.nothing_found)
        val emptySavedNewsText = view?.findViewById<TextView>(R.id.empty_saved_news)

        if (SavedNewsUtils.isEmpty(requireContext())) {
            nothingFoundAnimation?.visibility = View.VISIBLE
            emptySavedNewsText?.visibility = View.VISIBLE
        } else {
            nothingFoundAnimation?.visibility = View.GONE
            emptySavedNewsText?.visibility = View.GONE
        }

        savedArticlesList = SavedNewsUtils.getSavedArticlesList(requireContext()).toMutableList()

        recyclerView = view?.findViewById(R.id.saved_news_recycler_view)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        savedNewsAdapter = SavedNewsAdapter(savedArticlesList, requireContext(), object : SavedNewsAdapter.OnArticleClickListener {
            override fun onArticleClick(newsArticle: NewsArticle) {
                val intent = Intent(requireContext(), NewsContentActivity::class.java)
                intent.putExtra("ARTICLE_URL", newsArticle.url)
                intent.putExtra("ARTICLE_IMAGE", newsArticle.imageUrl)
                intent.putExtra("ARTICLE_CONTENT", newsArticle.content)
                intent.putExtra("ARTICLE_AUTHOR", newsArticle.author)
                intent.putExtra("ARTICLE_TITLE", newsArticle.title)
                intent.putExtra("ARTICLE_SOURCE", newsArticle.source?.name)
                intent.putExtra("ARTICLE_PUBLISH_DATE", newsArticle.publishedDate)
                intent.putExtra("ARTICLE_DATA", newsArticle)
                startActivityForResult(intent, requestCodeRefresh)
            }
        })

        recyclerView.adapter = savedNewsAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeRefresh && resultCode == Activity.RESULT_OK) {
            refreshSavedNews()
        }
    }

    private fun refreshSavedNews() {
        savedArticlesList.clear()
        savedArticlesList.addAll(SavedNewsUtils.getSavedArticlesList(requireContext()))
        savedNewsAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        view?.let { setSavedNews(it) }
    }


}

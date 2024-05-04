package com.news.reader.app.utils

object AppConstants {
    // API URL Constant (Can be changed as per the API Change Requirement)
    // Will only work if the API is publicly accessible
    const val BASE_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
    const val REQUEST_CODE_REFRESH = 101
    const val SHARED_PREFS_NAME = "SavedNewsPrefs"
    const val KEY_SAVED_ARTICLES = "saved_articles"
}
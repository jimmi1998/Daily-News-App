package com.news.reader.app.utils

import java.text.SimpleDateFormat
import java.util.Locale

object AppUtils {
    fun formatDate(dateString: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val date = dateString?.let {
            inputFormat.parse(it)
        }
        return date?.let {
            outputFormat.format(date)
        } ?: ""
    }
}
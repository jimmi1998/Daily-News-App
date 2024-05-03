package com.news.reader.app.model

import android.os.Parcel
import android.os.Parcelable

data class NewsArticle(
    val source: Source?,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val author: String?,
    val publishedDate: String?,
    val content: String?,
    val url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Source::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(source, flags)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeString(author)
        parcel.writeString(publishedDate)
        parcel.writeString(content)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsArticle> {
        override fun createFromParcel(parcel: Parcel): NewsArticle {
            return NewsArticle(parcel)
        }

        override fun newArray(size: Int): Array<NewsArticle?> {
            return arrayOfNulls(size)
        }
    }
}


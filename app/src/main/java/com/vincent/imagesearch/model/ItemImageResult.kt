package com.vincent.imagesearch.model

import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.vincent.imagesearch.AppController
import com.vincent.imagesearch.R
import kotlinx.android.parcel.Parcelize
import kotlin.math.ceil

/**
 * Created by Vincent on 2020/11/17.
 */
data class ItemImageResult(
    val total: Int,
    val totalHits: Int,
    val hits: List<Hit>
) {
    @Parcelize
    data class Hit(
        val id: Int,
        val tags: String,
        val type: String,
        val previewURL: String,
        val webformatURL: String,
        val largeImageURL: String,
        val likes: Int,
        val views: Int
    ) : Parcelable {
        fun getImageTypeColor(): Int {
            val colorRes = when (type) {
                Const.IMAGE_TYPE_PHOTO -> R.color.md_amber_800
                Const.IMAGE_TYPE_ILLUSTRATION -> R.color.md_light_blue_900
                Const.IMAGE_TYPE_VECTOR -> R.color.md_green_800
                else -> R.color.md_green_800
            }
            return ContextCompat.getColor(AppController.instance.applicationContext, colorRes)
        }
    }

    fun getNextPage(perPage: Int, currentPage: Int): Int? {
        return if (hasNextPage(perPage, currentPage)) {
            currentPage + 1
        }
        else {
            null
        }
    }

    fun getPreviousPage(perPage: Int, currentPage: Int): Int? {
        return if (currentPage > 1 && getTotalPages(perPage) > 0) {
            currentPage - 1
        }
        else {
            null
        }
    }

    private fun hasNextPage(perPage: Int, currentPage: Int): Boolean {
        return currentPage < getTotalPages(perPage)
    }

    private fun getTotalPages(perPage: Int): Int {
        return ceil(totalHits.toDouble() / perPage).toInt()
    }
}


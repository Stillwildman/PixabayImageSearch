package com.vincent.imagesearch.model

import android.provider.BaseColumns

/**
 * Created by Vincent on 2020/11/18.
 */
object DBParams : BaseColumns {

    const val DB_VERSION = 1

    const val DB_IMAGE_SEARCH = "ImageSearchDatabase"

    const val TABLE_SEARCH_RECORD = "SearchRecord"

    const val COLUMN_ID = "Id"

    const val COLUMN_KEY_WORDS = "KeyWords"

}
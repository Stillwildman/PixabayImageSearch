package com.vincent.imagesearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Created by Vincent on 2020/11/18.
 */

@Entity(tableName = DBParams.TABLE_SEARCH_RECORD)
class ItemSearchEntity {

    constructor(keyWords: String) {
        this.keyWords = keyWords
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBParams.COLUMN_ID)
    var id: Int = 0

    @ColumnInfo(name = DBParams.COLUMN_KEY_WORDS)
    var keyWords: String = ""
        set(value) {
        field = if (value.isEmpty()) Const.EMPTY_INPUT else URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
    }
    get() = URLDecoder.decode(field, StandardCharsets.UTF_8.toString())
}
package com.vincent.imagesearch.paging

import androidx.paging.DataSource
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.model.ItemImageResult

/**
 * Created by Vincent on 2020/11/17.
 */
class ImageDataSourceFactory internal constructor(
    private val keyWords: String,
    private val loadingCallback: OnLoadingCallback) : DataSource.Factory<Int, ItemImageResult.Hit>() {

    override fun create(): DataSource<Int, ItemImageResult.Hit> {
        return ImageDataSource(keyWords, loadingCallback)
    }

}
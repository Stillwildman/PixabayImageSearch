package com.vincent.imagesearch.paging

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.model.ItemImageResult

/**
 * Created by Vincent on 2020/11/17.
 */
class ImageDataRepo(private val keyWords: String, private val loadingCallback: OnLoadingCallback) : BasePagingConfig() {

    val imageHitList: LiveData<PagedList<ItemImageResult.Hit>>
        get() = LivePagedListBuilder(ImageDataSourceFactory(keyWords, loadingCallback), config)
            .setInitialLoadKey(getInitialPageKey())
            .build()

    override fun getPerPageSize(): Int = 50

    override fun getInitialPageKey(): Int = 50
}
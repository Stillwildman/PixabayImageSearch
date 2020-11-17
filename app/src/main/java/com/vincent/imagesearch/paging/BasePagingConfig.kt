package com.vincent.imagesearch.paging

import androidx.paging.PagedList

/**
 * For Paging config.
 */
abstract class BasePagingConfig {

    protected abstract fun getPerPageSize(): Int

    protected abstract fun getInitialPageKey(): Int

    protected val config: PagedList.Config
        get() = PagedList.Config.Builder()
            .setPageSize(getPerPageSize())
            .setInitialLoadSizeHint(getInitialPageKey())
            .setEnablePlaceholders(false)
            .build()
}
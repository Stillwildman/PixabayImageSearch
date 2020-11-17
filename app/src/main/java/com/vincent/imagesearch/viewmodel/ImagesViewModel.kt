package com.vincent.imagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.model.ItemImageResult
import com.vincent.imagesearch.paging.ImageDataRepo

/**
 * Created by Vincent on 2020/11/17.
 */
class ImagesViewModel : ViewModel(), OnLoadingCallback {

    val liveLoadingStatus = MutableLiveData<Boolean>()
    val liveErrorMessage = MutableLiveData<String>()

    fun getImagesSearching(keyWords: String): LiveData<PagedList<ItemImageResult.Hit>> {
        return ImageDataRepo(keyWords, this).imageHitList
    }

    override fun onLoadingStart() {
        liveLoadingStatus.value = true
    }

    override fun onLoadingEnds() {
        liveLoadingStatus.value = false
    }

    override fun onLoadingFailed(errorMessage: String?) {
        liveErrorMessage.value = errorMessage ?: ""
    }
}
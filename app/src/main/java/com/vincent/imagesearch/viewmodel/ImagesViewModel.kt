package com.vincent.imagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.vincent.imagesearch.callbacks.OnDataGetCallback
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.database.SearchRecordHelper
import com.vincent.imagesearch.model.ItemImageResult
import com.vincent.imagesearch.model.ItemSearchEntity
import com.vincent.imagesearch.paging.ImageDataRepo

/**
 * Created by Vincent on 2020/11/17.
 */
class ImagesViewModel : ViewModel(), OnLoadingCallback {

    val liveLoadingStatus = MutableLiveData<Boolean>()
    val liveErrorMessage = MutableLiveData<String>()

    val liveSearchRecordList = MutableLiveData<List<String>>()

    fun getImagesSearching(keyWords: String): LiveData<PagedList<ItemImageResult.Hit>> {
        SearchRecordHelper.checkSearchRecord(ItemSearchEntity(keyWords))
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

    fun observeSearchRecordListFlow() {
        SearchRecordHelper.observeSearchRecordListFlow(object : OnDataGetCallback<List<String>> {
            override fun onDataGet(item: List<String>?) {
                liveSearchRecordList.value = item
            }

            override fun onDataGetFailed(errorMessage: String?) {
                liveErrorMessage.value = errorMessage ?: ""
            }
        })
    }
}
package com.vincent.imagesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.database.ImageSearchDatabase
import com.vincent.imagesearch.model.Const
import com.vincent.imagesearch.model.ItemImageResult
import com.vincent.imagesearch.model.ItemSearchEntity
import com.vincent.imagesearch.paging.ImageDataRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Vincent on 2020/11/17.
 */
class ImagesViewModel : ViewModel(), OnLoadingCallback {

    private val tag = "ImageViewModel"

    val liveLoadingStatus = MutableLiveData<Boolean>()
    val liveErrorMessage = MutableLiveData<String>()

    val searchRecordList = mutableListOf<String>()

    fun getImagesSearching(keyWords: String): LiveData<PagedList<ItemImageResult.Hit>> {
        insertSearchRecord(ItemSearchEntity(keyWords))
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
        ImageSearchDatabase.getInstance().getSearchRecordDao().getRecordList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSubscriber<List<ItemSearchEntity>>() {
                override fun onNext(recordList: List<ItemSearchEntity>) {
                    Log.i(tag, "onNext!!! Count: ${recordList.size}")

                    if (recordList.size > Const.MAX_SEARCH_RECORD_NUMBER) {
                        deleteLastRecord()
                    }
                    else {
                        updateSearchRecordList(recordList)
                    }
                }

                override fun onError(e: Throwable?) {
                    Log.e(tag, "onError!!! ${e?.message}")
                    liveErrorMessage.postValue(e?.message ?: "")
                }

                override fun onComplete() {
                    Log.i(tag, "onComplete!!!")
                }
            })
    }

    private fun updateSearchRecordList(recordList: List<ItemSearchEntity>) {
        searchRecordList.clear()

        recordList.forEach { item ->
            Log.i(tag, "RecordName: ${item.keyWords}")
            searchRecordList.add(item.keyWords)
        }
    }

    private fun deleteLastRecord() {
        CompositeDisposable().add(ImageSearchDatabase.getInstance().getSearchRecordDao().deleteLastRecord()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ deletedCount ->
                Log.i(tag, "DeleteLastRecord! Success: ${deletedCount > 0}")
            }) { e ->
                Log.e(tag, "DeleteLastRecord onError!!! ${e.message}")
            })
    }

    private fun insertSearchRecord(item: ItemSearchEntity) {
        val deletingDisposable = ImageSearchDatabase.getInstance().getSearchRecordDao().deleteDuplicatedKeyWordRecord(item.keyWords)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ deletedCount ->
                Log.i(tag, "deleteDuplicatedKeyWordRecord! Success: ${deletedCount > 0}")
            }) { e ->
                Log.e(tag, "deleteDuplicatedKeyWordRecord onError!!! ${e.message}")
            }

        val insertingDisposable = ImageSearchDatabase.getInstance().getSearchRecordDao().addRecord(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i(tag, "Search Record Added!!!")
            }

        CompositeDisposable().addAll(deletingDisposable, insertingDisposable)
    }
}
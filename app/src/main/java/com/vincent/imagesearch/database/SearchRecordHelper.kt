package com.vincent.imagesearch.database

import android.util.Log
import com.vincent.imagesearch.callbacks.OnDataGetCallback
import com.vincent.imagesearch.model.Const
import com.vincent.imagesearch.model.ItemSearchEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Vincent on 2020/11/19.
 */
object SearchRecordHelper {

    private const val tag = "SearchRecordHelper"

    fun observeSearchRecordListFlow(dataGetCallback: OnDataGetCallback<List<String>>) {
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
                        updateSearchRecordList(recordList, dataGetCallback)
                    }
                }

                override fun onError(e: Throwable?) {
                    Log.e(tag, "onError!!! ${e?.message}")
                    dataGetCallback.onDataGetFailed(e?.message)
                }

                override fun onComplete() {
                    Log.i(tag, "onComplete!!!")
                }
            })
    }

    private fun updateSearchRecordList(recordList: List<ItemSearchEntity>, dataGetCallback: OnDataGetCallback<List<String>>) {
        val searchRecordList = mutableListOf<String>()

        recordList.forEach { item ->
            Log.i(tag, "RecordName: ${item.keyWords}")
            searchRecordList.add(item.keyWords)
        }

        dataGetCallback.onDataGet(searchRecordList)
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

    fun checkSearchRecord(item: ItemSearchEntity) {
        deleteDuplicatedKeyWord(item)
    }

    private fun deleteDuplicatedKeyWord(item: ItemSearchEntity) {
        CompositeDisposable().add(ImageSearchDatabase.getInstance().getSearchRecordDao().deleteDuplicatedKeyWordRecord(item.keyWords)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ deletedCount ->
                Log.i(tag, "deleteDuplicatedKeyWordRecord! Success: ${deletedCount > 0}")
                insertSearchRecord(item)
            }) { e ->
                Log.e(tag, "deleteDuplicatedKeyWordRecord onError!!! ${e.message}")
            })
    }

    private fun insertSearchRecord(item: ItemSearchEntity) {
        CompositeDisposable().add(ImageSearchDatabase.getInstance().getSearchRecordDao().addRecord(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i(tag, "Search Record Added!!!")
            })
    }

}
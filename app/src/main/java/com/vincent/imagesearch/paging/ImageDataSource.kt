package com.vincent.imagesearch.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.vincent.imagesearch.AppController
import com.vincent.imagesearch.R
import com.vincent.imagesearch.callbacks.OnDataGetCallback
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.model.ItemImageResult
import com.vincent.imagesearch.network.DataCallbacks
import com.vincent.imagesearch.utilities.AESHelper

/**
 * Created by Vincent on 2020/11/17.
 */
class ImageDataSource(private val keyWords: String, private val loadingCallback: OnLoadingCallback) : PageKeyedDataSource<Int, ItemImageResult.Hit>() {

    private val tag = "ImageDataSource"

    private val apiKey: String by lazy { initialKey() }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ItemImageResult.Hit>) {
        val initialPage = 1
        val perPage = params.requestedLoadSize

        Log.i(tag, "loadInitial!!! RequestLoadSize: $perPage")

        DataCallbacks.getPixabayImages(apiKey, keyWords, perPage, initialPage, object : OnDataGetCallback<ItemImageResult> {
            override fun onDataGet(item: ItemImageResult?) {
                item?.let {
                    callback.onResult(it.hits, null, it.getNextPage(perPage, initialPage))
                }
            }

            override fun onDataGetFailed(errorMessage: String?) {
                loadingCallback.onLoadingFailed(errorMessage)
            }
        }, loadingCallback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ItemImageResult.Hit>) {
        val perPage = params.requestedLoadSize
        val key = params.key

        Log.i(tag, "loadBefore!!! RequestLoadSize: $perPage Key: $key")

        DataCallbacks.getPixabayImages(apiKey, keyWords, perPage, key, object : OnDataGetCallback<ItemImageResult> {
            override fun onDataGet(item: ItemImageResult?) {
                item?.let {
                    callback.onResult(it.hits, it.getPreviousPage(perPage, key))
                }
            }

            override fun onDataGetFailed(errorMessage: String?) {
                loadingCallback.onLoadingFailed(errorMessage)
            }
        }, loadingCallback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ItemImageResult.Hit>) {
        val perPage = params.requestedLoadSize
        val key = params.key

        Log.i(tag, "loadAfter!!! RequestLoadSize: $perPage Key: $key")

        DataCallbacks.getPixabayImages(apiKey, keyWords, perPage, key, object : OnDataGetCallback<ItemImageResult> {
            override fun onDataGet(item: ItemImageResult?) {
                item?.let {
                    callback.onResult(it.hits, it.getNextPage(perPage, key))
                }
            }

            override fun onDataGetFailed(errorMessage: String?) {
                loadingCallback.onLoadingFailed(errorMessage)
            }
        }, loadingCallback)
    }

    private fun initialKey(): String {
        val apiKey = AESHelper.decrypt(AppController.instance.getString(R.string.pixabay_api_encrypted_key))
        Log.i(tag, "Decrypted key: $apiKey")
        return apiKey ?: ""
    }
}
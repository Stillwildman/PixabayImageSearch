package com.vincent.imagesearch.network

import android.util.Log
import com.vincent.imagesearch.callbacks.OnDataGetCallback
import com.vincent.imagesearch.callbacks.OnLoadingCallback
import com.vincent.imagesearch.model.ApiUrls
import com.vincent.imagesearch.model.ItemImageResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/**
 * Created by Vincent on 2020/11/17.
 */
object DataCallbacks {

    private const val TAG = "DataCallbacks"

    private fun getApiInterface(baseUrl: String = ApiUrls.BASE_PIXABAY_API): ApiInterface {
        return RetrofitAgent.getRetrofit(baseUrl).create(ApiInterface::class.java)
    }

    private fun <ResponseItem : Response<Item>, Item> getData(observable: Observable<ResponseItem>, callback: OnDataGetCallback<Item>, loadingCallback: OnLoadingCallback? = null) {
        observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(loading(loadingCallback))
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseItem> {
                override fun onSubscribe(d: Disposable) {
                    Log.i(TAG, "onSubscribe!!!")
                }

                override fun onNext(reponse: ResponseItem) {
                    Log.i(TAG, "onNext!!!")
                    callback.onDataGet(reponse.body())
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "onError!!! ${e.message}")
                    callback.onDataGetFailed(e.message ?: "")
                }

                override fun onComplete() {
                    Log.i(TAG, "onComplete!!!")
                }
            })
    }

    private fun <T> loading(loadingCallback: OnLoadingCallback?): ObservableTransformer<T, T> {
        return ObservableTransformer<T, T> { upstream ->
            upstream.doOnSubscribe {
                loadingCallback?.onLoadingStart()
            }.doFinally {
                loadingCallback?.onLoadingEnds()
            }
        }
    }

    fun getPixabayImages(apiKey:String, keyWords: String, perPage: Int, page: Int, dataCallback: OnDataGetCallback<ItemImageResult>, loadingCallback: OnLoadingCallback?) {
        val observable = getApiInterface().searchImages(apiKey, keyWords, perPage, page)
        getData(observable, dataCallback, loadingCallback)
    }
}
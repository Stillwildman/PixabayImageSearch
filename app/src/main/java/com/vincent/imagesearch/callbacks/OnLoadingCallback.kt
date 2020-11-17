package com.vincent.imagesearch.callbacks

/**
 * Created by Vincent on 2020/11/17.
 */
interface OnLoadingCallback {

    fun onLoadingStart()

    fun onLoadingEnds()

    fun onLoadingFailed(errorMessage: String?)

}
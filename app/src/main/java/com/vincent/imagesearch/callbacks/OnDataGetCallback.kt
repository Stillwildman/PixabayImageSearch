package com.vincent.imagesearch.callbacks

/**
 * Created by Vincent on 2020/11/17.
 */
interface OnDataGetCallback<Item> {

    fun onDataGet(item: Item?)

    fun onDataGetFailed(errorMessage: String?)

}
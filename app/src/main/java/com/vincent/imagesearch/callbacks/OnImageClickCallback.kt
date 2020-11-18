package com.vincent.imagesearch.callbacks

import com.vincent.imagesearch.model.ItemImageResult

/**
 * Created by Vincent on 2020/11/19.
 */
interface OnImageClickCallback {

    fun onImageClick(item: ItemImageResult.Hit)

}
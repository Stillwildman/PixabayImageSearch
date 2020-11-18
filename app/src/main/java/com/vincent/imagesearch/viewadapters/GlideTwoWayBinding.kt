package com.vincent.imagesearch.viewadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.vincent.imagesearch.R

/**
 * Created by Vincent on 2020/11/17.
 */
object GlideTwoWayBinding {

    private val options:RequestOptions by lazy {
        RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_place_holder_circle).centerCrop()
    }

    @JvmStatic
    @BindingAdapter("glideImage")
    fun setGlideImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(options)
            .into(imageView)
    }

}
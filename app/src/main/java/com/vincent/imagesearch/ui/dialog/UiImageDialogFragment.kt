package com.vincent.imagesearch.ui.dialog

import android.os.Bundle
import android.view.Window
import com.vincent.imagesearch.R
import com.vincent.imagesearch.databinding.DialogImageBinding
import com.vincent.imagesearch.model.Const
import com.vincent.imagesearch.model.ItemImageResult
import com.vincent.imagesearch.ui.bases.BaseDialogFragment

/**
 * Created by Vincent on 2020/11/19.
 */
class UiImageDialogFragment private constructor() : BaseDialogFragment<DialogImageBinding>() {

    companion object {
        fun newInstance(imageHit: ItemImageResult.Hit): UiImageDialogFragment {
            return UiImageDialogFragment().apply {
                arguments = Bundle().also { it.putParcelable(Const.BUNDLE_IMAGE_HIT, imageHit) }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_image

    override fun canCanceledOnTouchOutside(): Boolean = true

    override fun setDialogWindowAttrs(window: Window) {

    }

    override fun init() {
        getBundleAndSetItem()
    }

    private fun getBundleAndSetItem() {
        arguments?.getParcelable<ItemImageResult.Hit>(Const.BUNDLE_IMAGE_HIT)?.let {
            bindingView.imageHit = it
        }
    }
}
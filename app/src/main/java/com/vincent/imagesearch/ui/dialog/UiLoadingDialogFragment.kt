package com.vincent.imagesearch.ui.dialog

import android.view.Window
import com.vincent.imagesearch.R
import com.vincent.imagesearch.databinding.DialogLoadingCircleBinding
import com.vincent.imagesearch.ui.bases.BaseDialogFragment

/**
 * Created by Vincent on 2020/11/17.
 */
class UiLoadingDialogFragment : BaseDialogFragment<DialogLoadingCircleBinding>() {

    override fun getLayoutId(): Int = R.layout.dialog_loading_circle

    override fun setDialogWindowAttrs(window: Window) {

    }

    override fun init() {

    }
}
package com.vincent.imagesearch.ui.bases

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.vincent.imagesearch.R

/**
 * Created by Vincent on 2020/1/10.
 */
abstract class BaseDialogFragment<BindingView : ViewDataBinding> : DialogFragment() {

    @Suppress("PropertyName")
    protected val TAG = javaClass.simpleName

    protected abstract fun getLayoutId(): Int
    protected abstract fun setDialogWindowAttrs(window: Window)
    protected abstract fun init()

    protected lateinit var bindingView: BindingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog!!!")

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        setDialogSize()
    }

    private fun setDialogSize() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            attributes.run {
                dimAmount = 0.5f
                flags = flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            }
            attributes = attributes

            setDialogWindowAttrs(this)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume!!!")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindingView = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        init()
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause!!!")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop!!!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView!!!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy!!!")
    }
}
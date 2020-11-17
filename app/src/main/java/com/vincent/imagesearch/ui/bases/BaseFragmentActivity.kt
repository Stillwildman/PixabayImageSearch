package com.vincent.imagesearch.ui.bases

import android.util.Log
import androidx.annotation.Nullable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.vincent.imagesearch.model.Const
import com.vincent.imagesearch.ui.dialog.UiLoadingDialogFragment

/**
 * Created by Vincent on 2020/11/17.
 */
abstract class BaseFragmentActivity<BindingView : ViewDataBinding> : BaseActivity<BindingView>(), FragmentManager.OnBackStackChangedListener {

    private val fm : FragmentManager by lazy {
        supportFragmentManager.also { it.addOnBackStackChangedListener(this) }
    }

    override fun onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged!!! Count: ${fm.backStackEntryCount}")
    }

    private fun isFragmentsMoreThanOne() : Boolean = fm.backStackEntryCount > 1

    protected fun popBack(@Nullable backName : String?) {
        if (isFragmentsMoreThanOne()) {
            if (backName == null) {
                fm.popBackStackImmediate()
            }
            else {
                fm.popBackStackImmediate(backName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    private fun openDialogFragment(instance: DialogFragment, isLoadingDialog : Boolean) {
        fm.beginTransaction().let {
            if (isLoadingDialog) {
                instance.isCancelable = false
                instance.show(it, Const.LOADING_DIALOG_FRAGMENT)
            }
            else {
                it.addToBackStack(Const.BACK_COMMON_DIALOG)
                instance.show(it, Const.DIALOG_FRAGMENT)
            }
        }
    }

    protected fun dismissDialogFragment() {
        fm.findFragmentByTag(Const.DIALOG_FRAGMENT)?.let {
            (it as DialogFragment).dismiss()
        }
    }

    protected fun showLoadingDialog() {
        openDialogFragment(UiLoadingDialogFragment(), true)
    }

    protected fun dismissLoadingDialog() {
        fm.findFragmentByTag(Const.LOADING_DIALOG_FRAGMENT)?.let {
            (it as DialogFragment).dismiss()
        }
    }
}
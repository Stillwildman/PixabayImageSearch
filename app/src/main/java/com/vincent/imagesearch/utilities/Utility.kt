package com.vincent.imagesearch.utilities

import android.os.Process
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.StringRes
import com.vincent.imagesearch.AppController

object Utility {

    fun forceCloseTask() {
        Process.killProcess(Process.myPid())
    }

    fun toastShort(msg: String) {
        Toast.makeText(AppController.instance.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun toastShort(@StringRes msgResId: Int) {
        toastShort(AppController.instance.applicationContext.getString(msgResId))
    }

    fun toastLong(msg: String) {
        Toast.makeText(AppController.instance.applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    fun toastLong(@StringRes msgResId: Int) {
        toastLong(AppController.instance.applicationContext.getString(msgResId))
    }

    fun getScreenWidth(): Int {
        val dm: DisplayMetrics = AppController.instance.applicationContext.resources.displayMetrics
        return dm.widthPixels
    }

    fun getScreenHeight(): Int {
        val dm: DisplayMetrics = AppController.instance.applicationContext.resources.displayMetrics
        return dm.heightPixels
    }
}
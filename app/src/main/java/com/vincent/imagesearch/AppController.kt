package com.vincent.imagesearch

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class AppController : MultiDexApplication() {

    companion object {
        @get:Synchronized
        lateinit var instance: AppController
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun hideKeyboard(view: View) {
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

package com.vincent.imagesearch.utilities

import android.content.Context
import android.content.SharedPreferences
import com.vincent.imagesearch.AppController
import com.vincent.imagesearch.model.Const

/**
 * Created by Vincent on 2020/11/17.
 */
object SettingManager {

    private const val FILE_DEFAULT_PREFS = "General.pref"

    private const val PREF_VIEW_TYPE = "ViewType"

    private fun appContext(): Context = AppController.instance.applicationContext

    private fun getDefaultPrefs(): SharedPreferences {
        return appContext().getSharedPreferences(FILE_DEFAULT_PREFS, Context.MODE_PRIVATE)
    }

    fun isViewInRowType(): Boolean {
        return getDefaultPrefs().getBoolean(PREF_VIEW_TYPE, true)
    }

    fun setViewType(viewType: Int): Boolean {
        return getDefaultPrefs().edit().putBoolean(PREF_VIEW_TYPE, viewType == Const.VIEW_TYPE_SPAN_COUNT_ROW).commit()
    }
}
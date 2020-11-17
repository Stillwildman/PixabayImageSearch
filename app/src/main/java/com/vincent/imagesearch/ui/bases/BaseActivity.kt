package com.vincent.imagesearch.ui.bases

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.vincent.imagesearch.R
import com.vincent.imagesearch.utilities.MenuHelper
import com.vincent.imagesearch.utilities.Utility

/**
 * Created by Vincent on 2020/11/16.
 */
abstract class BaseActivity<BindingView : ViewDataBinding> : AppCompatActivity() {

    @Suppress("PropertyName")
    protected val TAG = javaClass.simpleName

    protected abstract fun getLayoutId(): Int

    protected abstract fun getToolbar(): Toolbar?

    protected abstract fun getLoadingView(): View?

    protected abstract fun getMenuOptions(): IntArray?

    protected abstract fun init()

    protected abstract fun onMenuOptionClick(itemId: Int)

    protected lateinit var bindingView: BindingView

    private var exitTime : Long = 0

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingView = DataBindingUtil.setContentView(this, getLayoutId())

        initToolbar()

        init()

        Log.d(TAG, "onCreate!!!")
    }

    private fun initToolbar() {
        getToolbar()?.apply {
            setSupportActionBar(this)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.menu_grid_view, menu)
        MenuHelper.setMenuOptions(menu, getMenuOptions())
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "onMenuOptionClick: ${item.itemId}")

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> onMenuOptionClick(item.itemId)
        }

        return true
    }

    protected fun setMenuOptions(actions: IntArray) {
        getToolbar()?.run {
            MenuHelper.setMenuOptions(menu, actions)
        }
    }

    protected fun showLoadingCircle() {
        getLoadingView()?.visibility = View.VISIBLE
    }

    protected fun hideLoadingCircle() {
        getLoadingView()?.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart!!!")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume!!!")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause!!!")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop!!!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy!!!")
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed!!!")

        // TODO Do some network check?
        finishAppWithinDoubleTap()
    }

    private fun finishAppWithinDoubleTap() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Utility.toastShort(R.string.confirm_to_exit)
            exitTime = System.currentTimeMillis()
        }
        else {
            Utility.forceCloseTask()
        }
    }
}
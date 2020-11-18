package com.vincent.imagesearch.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnKeyListener
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.vincent.imagesearch.AppController
import com.vincent.imagesearch.model.Const

/**
 * Created by Vincent on 2020/11/19.
 */
internal class SearchInputWidget : AppCompatAutoCompleteTextView, OnKeyListener, AdapterView.OnItemClickListener {

    private var callback: SearchInputCallback? = null

    interface SearchInputCallback {
        fun onSearchKeyPressed(text: String)
        fun onClickEmptyText()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        this.setOnKeyListener(this)
        this.onItemClickListener = this
    }

    fun setCallback(callback: SearchInputCallback) {
        this.callback = callback
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (getInputText().isEmpty()) {
                    callback?.onClickEmptyText()
                    return super.dispatchTouchEvent(event)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun getInputText(): String {
        return if (this.text.isNullOrEmpty()) "" else this.text.toString()
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
            AppController.instance.hideKeyboard(v)
            callback?.onSearchKeyPressed(getInputText())
            true
        }
        else false
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        (parent.getItemAtPosition(position) as String).let {
            if (it == Const.EMPTY_INPUT) {
                this.setText("")
                callback?.onSearchKeyPressed("")
            }
            else {
                callback?.onSearchKeyPressed(it)
            }
        }
    }
}
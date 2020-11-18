package com.vincent.imagesearch.ui

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.vincent.imagesearch.AppController
import com.vincent.imagesearch.R
import com.vincent.imagesearch.databinding.ActivityMainBinding
import com.vincent.imagesearch.model.Const
import com.vincent.imagesearch.model.ItemImageResult
import com.vincent.imagesearch.ui.adapters.ImageListAdapter
import com.vincent.imagesearch.ui.bases.BaseFragmentActivity
import com.vincent.imagesearch.utilities.MenuActions
import com.vincent.imagesearch.utilities.SettingManager
import com.vincent.imagesearch.utilities.Utility
import com.vincent.imagesearch.viewmodel.ImagesViewModel
import com.vincent.imagesearch.widgets.SearchInputWidget

class UiMainActivity : BaseFragmentActivity<ActivityMainBinding>(), View.OnClickListener, TextWatcher, SearchInputWidget.SearchInputCallback {

    private val imageViewModel by lazy { ViewModelProvider(this).get(ImagesViewModel::class.java) }

    private var isUiBlockedLoading = true

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getToolbar(): Toolbar? = bindingView.toolbar

    override fun getLoadingView(): View? = bindingView.loadingCircle

    override fun getMenuOptions(): IntArray? {
        return intArrayOf(
            if (SettingManager.isViewInRowType()) MenuActions.ACTION_GRID_VIEW_TYPE else MenuActions.ACTION_ROW_VIEW_TYPE
        )
    }

    override fun init() {
        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        imageViewModel.let {
            it.liveLoadingStatus.observe(this, { isLoading ->
                setLoading(isLoading)
            })
            it.liveErrorMessage.observe(this, { errorMessage ->
                Utility.toastLong(errorMessage)
            })
            it.observeSearchRecordListFlow()
        }
    }

    private fun initViews() {
        val spanCount = if (SettingManager.isViewInRowType()) Const.VIEW_TYPE_SPAN_COUNT_ROW else Const.VIEW_TYPE_SPAN_COUNT_GRID

        bindingView.includeContent.recyclerImages.let {
            it.layoutManager = GridLayoutManager(
                AppController.instance.applicationContext,
                spanCount
            )
            it.adapter = ImageListAdapter(it.layoutManager as GridLayoutManager)
        }

        bindingView.buttonSearch.setOnClickListener(this)

        bindingView.editInput.let {
            it.addTextChangedListener(this)
            it.setDropDownBackgroundResource(R.drawable.background_search_adapter)
            it.setCallback(this)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            if (isUiBlockedLoading) {
                showLoadingDialog()
            }
            else {
                showLoadingCircle()
            }
        }
        else {
            dismissLoadingDialog()
            hideLoadingCircle()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_search -> {
                AppController.instance.hideKeyboard(v)
                searchImages(bindingView.editInput.getInputText())
            }
        }
    }

    private fun searchImages(keyWords: String) {
        isUiBlockedLoading = true

        imageViewModel.getImagesSearching(keyWords).observe(
            this,
            { pagedList: PagedList<ItemImageResult.Hit>? ->
                isUiBlockedLoading = false
                updateImageList(pagedList)
            })
    }

    private fun updateImageList(pagedList: PagedList<ItemImageResult.Hit>?) {
        getImageListAdapter()?.updateList(pagedList)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.i(TAG, "onTextChanged: ${s?.toString()} start: $start before: $before count: $count")
        if (count == 0) {
            bindingView.editInput.postDelayed({
                showAutoCompleteView()
            }, 100)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        Log.i(TAG, "afterTextChanged: ${s?.toString()}")
    }

    private fun showAutoCompleteView() {
        bindingView.editInput.let {
            val arrayAdapter = ArrayAdapter(this, R.layout.inflate_search_record_text, imageViewModel.searchRecordList)
            it.setAdapter(arrayAdapter)
            it.showDropDown()
        }
    }

    private fun getImageListAdapter(): ImageListAdapter? {
        return bindingView.includeContent.recyclerImages.adapter?.let {
            it as ImageListAdapter
        }
    }

    override fun onSearchKeyPressed(text: String) {
        searchImages(text)
    }

    override fun onClickEmptyText() {
        showAutoCompleteView()
    }

    override fun onMenuOptionClick(itemId: Int) {
        when (itemId) {
            MenuActions.ACTION_ROW_VIEW_TYPE -> {
                changeViewTypeAndSetting(
                    Const.VIEW_TYPE_SPAN_COUNT_ROW,
                    MenuActions.ACTION_GRID_VIEW_TYPE
                )
            }
            MenuActions.ACTION_GRID_VIEW_TYPE -> {
                changeViewTypeAndSetting(
                    Const.VIEW_TYPE_SPAN_COUNT_GRID,
                    MenuActions.ACTION_ROW_VIEW_TYPE
                )
            }
        }
    }

    private fun changeViewTypeAndSetting(viewType: Int, menuAction: Int) {
        if (SettingManager.setViewType(viewType)) {
            setMenuOptions(intArrayOf(menuAction))
            getImageListAdapter()?.changeViewType()
        }
    }
}
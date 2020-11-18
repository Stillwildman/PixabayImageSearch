package com.vincent.imagesearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vincent.imagesearch.R
import com.vincent.imagesearch.callbacks.OnImageClickCallback
import com.vincent.imagesearch.databinding.InflateImageGridBinding
import com.vincent.imagesearch.databinding.InflateImageRowBinding
import com.vincent.imagesearch.model.Const
import com.vincent.imagesearch.model.ItemImageResult

/**
 * Created by Vincent on 2020/11/17.
 */
class ImageListAdapter(private val layoutManager: GridLayoutManager, private val imageClickCallback: OnImageClickCallback) :
    PagedListAdapter<ItemImageResult.Hit, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ItemImageResult.Hit>() {
        override fun areItemsTheSame(oldItem: ItemImageResult.Hit, newItem: ItemImageResult.Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemImageResult.Hit, newItem: ItemImageResult.Hit): Boolean {
            return oldItem.id == newItem.id && oldItem.tags == newItem.tags
        }
    })
{
    fun updateList(pagedImageList: PagedList<ItemImageResult.Hit>?) {
        pagedImageList?.let {
            submitList(it)
        }
    }

    /** @return The viewType after changed. */
    fun changeViewType(): Int {
        if (layoutManager.spanCount == Const.VIEW_TYPE_SPAN_COUNT_ROW) {
            layoutManager.spanCount = Const.VIEW_TYPE_SPAN_COUNT_GRID
        }
        else {
            layoutManager.spanCount = Const.VIEW_TYPE_SPAN_COUNT_ROW
        }

        notifyItemRangeChanged(0, itemCount)

        return layoutManager.spanCount
    }

    override fun getItemViewType(position: Int): Int {
        return layoutManager.spanCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Const.VIEW_TYPE_SPAN_COUNT_ROW -> {
                val bindingView = DataBindingUtil.inflate<InflateImageRowBinding>(LayoutInflater.from(parent.context), R.layout.inflate_image_row, parent, false)
                ImageRowViewHolder(bindingView)
            }
            else -> {
                val bindingView = DataBindingUtil.inflate<InflateImageGridBinding>(LayoutInflater.from(parent.context), R.layout.inflate_image_grid, parent, false)
                ImageGridViewHolder(bindingView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageRowViewHolder -> {
                holder.bindingView.imageHit = getItem(position)
                holder.bindingView.onImageClick = imageClickCallback
            }
            is ImageGridViewHolder -> {
                holder.bindingView.imageHit = getItem(position)
                holder.bindingView.onImageClick = imageClickCallback
            }
        }
    }

    inner class ImageRowViewHolder(val bindingView: InflateImageRowBinding) : RecyclerView.ViewHolder(bindingView.root)

    inner class ImageGridViewHolder(val bindingView: InflateImageGridBinding) : RecyclerView.ViewHolder(bindingView.root)
}
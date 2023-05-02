package com.example.aljabermall.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aljabermall.R
import com.example.aljabermall.databinding.ItemCateAdapterBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.models.CategoriesItems

class CateProductAdapter(
    private val onProductListener: OnProductListener,
    private val onChangeCartListener: OnChangeCartListener,
    private val mContext: Context,
    private val onCateListener: (Bundle) -> Unit
) : ListAdapter<CategoriesItems, RecyclerView.ViewHolder>(DifUtilCallback) {

    private var language = ""

    companion object {
        var CAT_ID = 0
    }

    object DifUtilCallback : DiffUtil.ItemCallback<CategoriesItems>() {
        override fun areItemsTheSame(oldItem: CategoriesItems, newItem: CategoriesItems): Boolean {
            return oldItem.category_id == newItem.category_id
        }

        override fun areContentsTheSame(
            oldItem: CategoriesItems,
            newItem: CategoriesItems
        ): Boolean {
            return oldItem.items == newItem.items
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        language = HelperUtils.getLang(recyclerView.context)
    }

    inner class CateProductHolder(binding: ItemCateAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val categoryName = binding.categoryName
        private val productRecycler = binding.productRecycler

        val productAdapter = ProductAdapter(onProductListener, onChangeCartListener,"0", mContext)
        init {
            productRecycler.adapter = productAdapter
        }

    }

    inner class CateHolder(binding: ItemCateAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val categoryName = binding.categoryName
        private val productRecycler = binding.productRecycler
        val cateAdapter = CategoryAdapter(onCateListener)

        init {
            categoryName.text = binding.root.context.getString(R.string.categories)
            productRecycler.adapter = cateAdapter

        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item.category_id == -1)
            ViewType.CATEGORIES
        else
            ViewType.PRODUCTS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCateAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return if (viewType == ViewType.CATEGORIES) {

            val cateHolder = CateHolder(binding)
            cateHolder
        }
        else {
            val productHolder = CateProductHolder(binding)
            productHolder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listCate = currentList

        when {
            getItemViewType(position) == ViewType.CATEGORIES -> {
                val cateHolder = holder as CateHolder
                val categories = mutableListOf<CategoriesItems>()
                categories.addAll(listCate)
                categories.removeAt(0)
                cateHolder.cateAdapter.submitList(categories)
//                cateHolder.itemView.setOnClickListener {
                CAT_ID = categories[position].category_id
                Log.i("CAT", "onBindViewHolder: " + CAT_ID)
//               }

            }
            getItemViewType(position) == ViewType.PRODUCTS -> {
                val categoryItem = listCate[position]
                val productHolder = holder as CateProductHolder

                if(categoryItem.items.isEmpty()){
                    productHolder.categoryName.hide()
                }

                productHolder.categoryName.text = categoryItem.category_name_ar
                productHolder.productAdapter.submitList(categoryItem.items)

            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    object ViewType {
        const val CATEGORIES = 0
        const val PRODUCTS = 1
    }
}
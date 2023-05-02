package com.example.aljabermall.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.aljabermall.R
import com.example.aljabermall.databinding.ItemCateBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.models.CategoriesItems

class CategoryAdapter(
    private val onCateListener: (Bundle) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    private var language = ""

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        language = HelperUtils.getLang(recyclerView.context)
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<CategoriesItems>() {
        override fun areItemsTheSame(oldItem: CategoriesItems, newItem: CategoriesItems): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CategoriesItems,
            newItem: CategoriesItems
        ): Boolean {
            return oldItem.category_id == newItem.category_id && oldItem.items == newItem.items
        }

    }

    private val listDiffer = AsyncListDiffer(this, diffUtilCallback)

    fun submitList(listProduct: List<CategoriesItems>) {
        listDiffer.submitList(listProduct)
    }

    inner class CategoryHolder(binding: ItemCateBinding) : RecyclerView.ViewHolder(binding.root) {
        val cateTitle = binding.cateTitle
        val cateImage = binding.cateImage

        init {

            binding.root.setOnClickListener {
                val bundle = Bundle()
                val products = listDiffer.currentList[bindingAdapterPosition].items
                bundle.putParcelableArrayList("products", ArrayList(products))
                onCateListener(bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = ItemCateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val model = listDiffer.currentList[position]
            holder.cateTitle.text = model.category_name_ar


Log.d("ImageLink",HelperUtils.BASE_URL + model.category_image)
        holder.cateImage.load(HelperUtils.BASE_URL + model.category_image) {
            placeholder(R.drawable.image)
            error(R.drawable.image)
            scale(Scale.FIT)
            crossfade(true)
        }
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }
}
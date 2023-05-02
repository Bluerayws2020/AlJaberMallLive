package com.example.aljabermall.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class GenericAdapter<T, D : ViewBinding?>(private val mArrayList: List<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun onBindData(model: T?, dataBinding: D?)
    abstract fun onItemClick(model: T?)
    abstract fun getViewBinding(viewGroup: ViewGroup?): D
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(getViewBinding(parent))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(
            mArrayList[position],
            (holder as GenericAdapter<*, *>.ItemViewHolder).mViewBinding as D
        )
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    fun getItem(position: Int): T {
        return mArrayList[position]
    }

    internal inner class ItemViewHolder(binding: D) :
        RecyclerView.ViewHolder(binding?.root!!) {
        val mViewBinding: D = binding

        init {
            mViewBinding?.root?.setOnClickListener { onItemClick(getItem(bindingAdapterPosition)) }
        }
    }
}
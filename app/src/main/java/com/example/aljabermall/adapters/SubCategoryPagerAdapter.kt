package com.example.aljabermall.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aljabermall.fragments.ProductFragment
import com.example.aljabermall.models.ProductItems

class SubCategoryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    private val productsList: MutableList<ArrayList<ProductItems>> = ArrayList()

    fun addProductsList(products: List<ProductItems>) {
        productsList.add(ArrayList(products))
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun createFragment(position: Int): Fragment {
        val productFragment = ProductFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("products", productsList[position])
        productFragment.arguments = bundle
        return productFragment
    }

//    override fun getCount(): Int {
//        return productsList.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//
//    }
//
//    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        TODO("Not yet implemented")
//    }


}
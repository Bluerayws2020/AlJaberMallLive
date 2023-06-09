package com.example.aljabermall.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aljabermall.fragments.ProductFragment
import com.example.aljabermall.models.ProductItems

class CategoryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :FragmentStateAdapter(fragmentManager,lifecycle) {

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

}
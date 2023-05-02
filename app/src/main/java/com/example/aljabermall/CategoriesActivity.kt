package com.example.aljabermall

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.adapters.CategoryPagerAdapter
import com.example.aljabermall.adapters.SubCategoryPagerAdapter
import com.example.aljabermall.databinding.ActivityCategoriesBinding
import com.example.aljabermall.fragments.CATE_ID
import com.example.aljabermall.fragments.CategoriesFragment
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.CategoriesItems
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.ProductsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding
    private val productVM by viewModels<ProductsViewModel>()
    private var cateId: Int? = null
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = getString(R.string.categories)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cateId = intent.extras?.getInt(CATE_ID)
        HelperUtils.setDefaultLanguage(this,"ar")

        productVM.getProductsCate().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupProductsPager(result.data.categories_with_items)
                        Log.d("categories_with_items",result.data.categories_with_items.toString())
//                        productVM.retrieveProductSubCategories( CategoriesFragment.CateID.toString() )

//                        Log.d("category = ", "onCreate: " + CategoriesFragment.CateID)

                    } else {
                        binding.messageCategories.show()
                        Log.d("categories_with_items",result.data.categories_with_items.toString())

                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.messageCategories.show()
                }
            }
            binding.progressBarTab.hide()
        }



//         sub category response
        productVM.getSubCategory().observe(this) { result ->
            binding.progressBarTab.show()

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        if (result.data.categories_with_items.isEmpty()){
                            binding.noData.show()
                        }else {
                            setupSubCategoryPager(result.data.categories_with_items)
                        }

                        binding.progressBarTab.hide()
                        binding.subpd.hide()




                    } else {
                        binding.messageCategories.show()

                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.messageCategories.show()
                }
            }
            binding.progressBarTab.hide()
        }





        productVM.retrieveProductCategories()
        productVM.retrieveProductSubCategories(cateId.toString())

        //         defalut sub Catgeroy

        language = HelperUtils.getLang(this)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    //Hundle click to get sub category
    private fun setupProductsPager(list: List<CategoriesItems?>) {
        val categoryPagerAdapter = CategoryPagerAdapter(supportFragmentManager, lifecycle)

        val tabListTitle: MutableList<String> = ArrayList()
        list.forEach { cate ->
            cate?.let {

                tabListTitle.add(it.category_name_ar)
                categoryPagerAdapter.addProductsList(it.items)
            }
        }


        binding.pagerProducts.adapter = categoryPagerAdapter




        TabLayoutMediator(
            binding.tabLayoutCategory,
            binding.pagerProducts
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabListTitle[position]
            cateId = list[position]?.category_id
            tab.view.setOnClickListener{
                Log.d("tabclick","clisk")
                productVM.retrieveProductSubCategories(list[position]?.category_id.toString())
                binding.subpd.show()
                tabListTitle.clear()

                binding.tabLayoutCategory.getTabAt(position)?.select()

            }
        }.attach()

        binding.tabLayoutCategory.visibility = View.VISIBLE

        val pos = list.indexOfFirst {
            it?.category_id == cateId


        }

//    binding.tabLayoutCategory



        binding.pagerProducts.setCurrentItem(pos, false)


    }
    //category
    private fun setupSubCategoryPager(list: List<CategoriesItems?>) {

        val categoryPagerAdapter = SubCategoryPagerAdapter(supportFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()
        list.forEach { cate ->
            cate?.let {

                tabListTitle.add(it.category_name_ar)
                categoryPagerAdapter.addProductsList(it.items)



            }
        }

        if (list.size == 1){
            binding.noData.show()
            binding.tabLayoutSubCategory.hide()
            binding.tabLayoutSubCategory.getTabAt(1)?.view?.hide()
        }else {
            binding.noData.hide()
            binding.tabLayoutSubCategory.show()


        }
        Log.d("arr couns",list.size.toString())

        binding.pagerProducts.adapter = categoryPagerAdapter

//
        TabLayoutMediator(
            binding.tabLayoutSubCategory,
            binding.pagerProducts
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabListTitle[position]
//            binding.tabLayoutCategory.getTabAt(position)?.select()

//tab.view.setOnClickListener{
//    productVM.retrieveProductSubCategories(list[position]?.category_id.toString())
//}
        }.attach()











        binding.tabLayoutSubCategory.setOnClickListener {

        }

        binding.tabLayoutSubCategory.visibility = View.VISIBLE

//        val pos = list.indexOfFirst {
//            it?.category_id == cateId
//
//        }

//        binding.pagerProducts.setCurrentItem(pos, false)
//      binding.pagerProducts.setOnClickListener{
//          Log.d("Hello for tab layout","1")
//    }

    }




    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}
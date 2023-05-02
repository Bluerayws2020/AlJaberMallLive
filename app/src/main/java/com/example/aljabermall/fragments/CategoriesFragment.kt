package com.example.aljabermall.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aljabermall.CategoriesActivity
import com.example.aljabermall.R
import com.example.aljabermall.adapters.GenericAdapter
import com.example.aljabermall.databinding.FragmentCategoriesBinding
import com.example.aljabermall.databinding.ItemCategoryBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.CategoryModel
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.ProductsViewModel

const val CATE_ID = "cate_id"

class CategoriesFragment : BaseFragment<FragmentCategoriesBinding, ProductsViewModel>() {
    override val viewModel by viewModels<ProductsViewModel>()

    companion object{
        var CateID = 0
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoriesBinding {
        return FragmentCategoriesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)

        if (!isNetWorkAvailable()) {
            binding.messageCate.show()
            binding.messageCate.text = getString(R.string.error)
        }
        HelperUtils.setDefaultLanguage(requireContext(),"ar")

        viewModel.getCategories().observe(viewLifecycleOwner) { result ->
            binding.progressBarCate.hide()
            binding.swipeToRefresh.isRefreshing = false
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupCateRecycler(result.data.categories)
                        Log.d("result.data.categories",result.data.categories.toString())


                    } else {
                        binding.messageCate.show()
                        binding.messageCate.text = result.data.status.msg
                    }


                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Log.d("Stringss",result.exception.toString())
                    binding.messageCate.show()
                }
            }
        }


        viewModel.retrieveCategories()

        binding.swipeToRefresh.setOnRefreshListener {
            binding.categoriesRecycler.adapter = null
            //binding.progressBarCate.show()
            binding.messageCate.hide()
            viewModel.retrieveCategories()
        }

        binding.includeTab.backButton.setOnClickListener {
            navController.popBackStack()
        }

        binding.includeTab.tabTitle.text = getString(R.string.all_categories)

    }

    private fun setupCateRecycler(categories: List<CategoryModel>) {
        val language = HelperUtils.getLang(mContext)
        val categoryAdapter =
            object : GenericAdapter<CategoryModel, ItemCategoryBinding>(categories) {
                override fun onBindData(model: CategoryModel?, dataBinding: ItemCategoryBinding?) {
                        dataBinding?.categoryTitle?.text = model?.category_name_ar

//                    dataBinding?.cateImage?.load(model?.category_image) {
//                        placeholder(R.drawable.image)
//                        error(R.drawable.image)
//                        scale(Scale.FILL)
//                    }

                    Glide.with(this@CategoriesFragment)
                        .load(HelperUtils.BASE_URL + model?.category_image)
                        .placeholder(R.drawable.ic_profile_user)
                        .error(R.drawable.ic_profile_user)
                        .into(dataBinding?.cateImage!!)

                }


                override fun onItemClick(model: CategoryModel?) {
                    val intentCategories = Intent(mContext, CategoriesActivity::class.java)
                    val bundle = Bundle()
                    bundle.putInt(CATE_ID, model?.id!!)
                    CateID = model.id
                    Log.d("Model_ID", "onItemClick: $CateID")
                    intentCategories.putExtras(bundle)
                    startActivity(intentCategories)
                }

                override fun getViewBinding(viewGroup: ViewGroup?): ItemCategoryBinding {
                    return ItemCategoryBinding.inflate(
                        LayoutInflater.from(viewGroup?.context),
                        viewGroup,
                        false
                    )
                }

            }

        val horizontalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.categoriesRecycler.layoutManager = horizontalLayoutManager

        binding.categoriesRecycler.adapter = categoryAdapter

    }
}
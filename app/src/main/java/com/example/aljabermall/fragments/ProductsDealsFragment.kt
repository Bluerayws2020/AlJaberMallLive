package com.example.aljabermall.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.blueray.aljabermall.PRODUCT_KEY
import com.blueray.aljabermall.ProductDetailActivity
import com.example.aljabermall.R
import com.example.aljabermall.adapters.DealesAdapter
import com.example.aljabermall.adapters.OnProductListener
import com.example.aljabermall.databinding.FragmentProductsDealsBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.ProductItems
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.FavouriteViewModel
import com.example.aljabermall.viewmodels.ProductsViewModel


class ProductsDealsFragment : BaseFragment<FragmentProductsDealsBinding, ProductsViewModel>() {
    override val viewModel by viewModels<ProductsViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    private var productAdapter: DealesAdapter? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductsDealsBinding {
        return FragmentProductsDealsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        if (!isNetWorkAvailable()) {
            binding.messageDeals.show()
            binding.messageDeals.text = getString(R.string.error)
        }

        HelperUtils.setDefaultLanguage(requireContext(),"ar")

        viewModel.getProudectDeals().observe(viewLifecycleOwner) { result ->
            binding.progressBarDeals.hide()
            binding.swipeToRefresh.isRefreshing = false
            when (result) {
                is NetworkResults.Success -> {
                    Log.d("sTTTTuTTTTS",result.toString())
                    if (result.data.status.status == 1) {
                        productAdapter?.submitList(result.data.items)
                    } else {
                        binding.messageDeals.show()
                        binding.messageDeals.text = result.data.status.msg
                    }
                }
                is NetworkResults.Error -> {
                    Log.d("sTTTTuTTTTS",result.toString())

                    result.exception.printStackTrace()
                    binding.messageDeals.show()
                }
            }
        }
        setupDealsRecycler()
        viewModel.retriveproductsDeals("1")

        favouriteViewModel.getAddToFavouriteMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    viewModel.retrieveOnSaleProducts()
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }

        favouriteViewModel.getRemoveFavouriteMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    viewModel.retrieveOnSaleProducts()
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }


        cartViewModel.getAddToCartMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }


        binding.swipeToRefresh.setOnRefreshListener {
            binding.messageDeals.hide()
            viewModel.retriveproductsDeals("1")
        }
        binding.includeTab.backButton.setOnClickListener {
            navController.popBackStack()
        }
        binding.includeTab.tabTitle.text = getString(R.string.deals)
    }

    private fun setupDealsRecycler() {
        productAdapter = DealesAdapter(object : OnProductListener {
            override fun addToCart(price: Double?, pid: Int?, quantity: String) {
                showProgress()
                cartViewModel.addToCart(price!!,pid.toString(), quantity)
            }

            override fun addToFavourite(pid: Int) {
                showProgress()
                favouriteViewModel.addToFavourite(pid.toString())
            }

            override fun removeFromFavourite(favId: Int) {
                showProgress()
                favouriteViewModel.removeFavourite(favId.toString())
            }

            override fun showDetails(product: ProductItems) {
                val intentProductDetail = Intent(mContext, ProductDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PRODUCT_KEY, product)
                intentProductDetail.putExtras(bundle)
                startActivity(intentProductDetail)
            }

        }, applicationContext())
//
//        binding.productDealsRecycler.addItemDecoration(
//            DividerItemDecoration(
//                mContext,
//                RecyclerView.VERTICAL
//            )
//        )




//        val horizontalLayoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.categoriesRecycler.layoutManager = GridLayoutManager(this ,2)
        binding.productDealsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productDealsRecycler.adapter = productAdapter
    }
}
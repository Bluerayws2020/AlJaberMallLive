package com.example.aljabermall.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.blueray.aljabermall.PRODUCT_KEY
import com.blueray.aljabermall.ProductDetailActivity
import com.example.aljabermall.R
import com.example.aljabermall.adapters.HorizontalProductAdapter
import com.example.aljabermall.adapters.OnProductListener
import com.example.aljabermall.databinding.FragmentProductBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.ProductItems
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.FavouriteViewModel

class ProductFragment : BaseFragment<FragmentProductBinding, FavouriteViewModel>() {

    override val viewModel by viewModels<FavouriteViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()

    //to update product list on add & remove from favourite
    private var productsAdapter: HorizontalProductAdapter? = null
    private var productId = 0
    private var favouriteId = 0

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductBinding {
        return FragmentProductBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productList = arguments?.getParcelableArrayList<ProductItems>("products")
        val isNavAvailable = arguments?.getBoolean("is_nav_available")
        HelperUtils.setDefaultLanguage(requireContext(),"ar")

        if (isNavAvailable == true) {
            val navController = Navigation.findNavController(view)
            binding.includeTab.backButton.setOnClickListener {
                navController.popBackStack()
            }
        } else
            binding.includeTab.frameTab.hide()

        viewModel.getAddToFavouriteMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        val position = productList?.indexOfFirst { it.id == productId }
                        position?.let {
                            productList[it].favorite_status = 0
                            productList[it].favorite_status = 1
                            productsAdapter?.notifyItemChanged(it)
                        }
                    }
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(getString(R.string.error))
                }
            }
        }

        viewModel.getRemoveFavouriteMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        val position = productList?.indexOfFirst { it.favorite_status == favouriteId }
                        position?.let {
                            productList[it]?.favorite_status = 0
                            productList[it].favorite_status = 0
                            productsAdapter?.notifyItemChanged(it)
                        }
                    }
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

        binding.includeTab.tabTitle.text = getString(R.string.products)

        if (productList.isNullOrEmpty())
            binding.messageProducts.show()
        else {
            setupRecycler()
            productsAdapter?.submitList(productList)
        }
    }

    private fun setupRecycler() {
        productsAdapter = HorizontalProductAdapter(object : OnProductListener {


            override fun addToCart(price: Double?, pid: Int?, quantity: String) {
                showProgress()
                cartViewModel.addToCart(price?.toDouble()!!,productId.toString(), quantity)
            }

            override fun addToFavourite(pid: Int) {
                productId = pid
                showProgress()
                viewModel.addToFavourite(pid.toString())
            }

            override fun removeFromFavourite(favId: Int) {
                favouriteId = favId
                showProgress()
                viewModel.removeFavourite(favId.toString())
            }

            override fun showDetails(product: ProductItems) {
                val intentProductDetail = Intent(mContext, ProductDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PRODUCT_KEY, product)
                intentProductDetail.putExtras(bundle)
                startActivity(intentProductDetail)
            }

        })

        binding.productsRecycler.addItemDecoration(
            DividerItemDecoration(
                mContext,
                RecyclerView.VERTICAL
            )
        )
        binding.productsRecycler.adapter = productsAdapter
    }
}
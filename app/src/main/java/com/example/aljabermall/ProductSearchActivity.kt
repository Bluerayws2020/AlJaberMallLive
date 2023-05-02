package com.example.aljabermall

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import com.blueray.aljabermall.PRODUCT_KEY
import com.blueray.aljabermall.ProductDetailActivity
import com.example.aljabermall.adapters.OnChangeCartListener
import com.example.aljabermall.adapters.OnProductListener
import com.example.aljabermall.adapters.ProductAdapter
import com.example.aljabermall.databinding.ActivityProductSearchBinding
import com.example.aljabermall.fragments.ProgressDialogFragment
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.ProductItems
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.FavouriteViewModel
import com.example.aljabermall.viewmodels.ProductsViewModel

class ProductSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductSearchBinding
    private val productVM by viewModels<ProductsViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    private var productSearchAdapter: ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")

        supportActionBar?.title = getString(R.string.search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupSearchProducts()

        productVM.getSearchProducts().observe(this) { result ->
            binding.progressBarSearch.hide()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        productSearchAdapter?.submitList(result.data.search_items)
                    } else {
                        binding.messageSearch.apply {
                            show()
                            text = result.data.status.msg
                        }
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.messageSearch.apply {
                        show()
                        text = getString(R.string.error)
                    }
                }
            }
        }

        favouriteViewModel.getAddToFavouriteMessage().observe(this) { result ->
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                hideProgress()
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.status.status == 1) {
                            productVM.searchProducts(binding.searchEt.text.toString())
                        }
                        toast(result.data.status.msg)
                    }
                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                        toast(getString(R.string.error))
                    }
                }
            }
        }

        favouriteViewModel.getRemoveFavouriteMessage().observe(this) { result ->
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                hideProgress()
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.status.status == 1) {
                            productVM.searchProducts(binding.searchEt.text.toString())
                        }
                        toast(result.data.status.msg)
                    }
                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                        toast(getString(R.string.error))
                    }
                }
            }
        }

        cartViewModel.getAddToCartMessage().observe(this) { result ->
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
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
        }

        binding.searchBtn.setOnClickListener {
            searchProducts(binding.searchEt.text.toString())
        }

        binding.searchEt.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchProducts(v.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

    }

    private fun searchProducts(searchTerm: String) {
        HelperUtils.hideKeyBoard(this)
        if (searchTerm.isNotEmpty()) {
            binding.messageSearch.hide()
            binding.progressBarSearch.show()
            productVM.searchProducts(searchTerm)
        } else
            toast(getString(R.string.search_empty))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun showProgress() {
        ProgressDialogFragment().show(supportFragmentManager, "progress_dialog")
    }

    private fun hideProgress() {
        supportFragmentManager.findFragmentByTag("progress_dialog")?.let {
            if (it.isAdded)
                (it as ProgressDialogFragment).dismiss()
        }
    }

    private fun setupSearchProducts() {
        productSearchAdapter = ProductAdapter(object : OnProductListener {
            override fun addToCart(price: Double?, pid: Int?, quantity: String) {

                cartViewModel.addToCart(price!!, pid.toString(),quantity.toString())


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
                val intentProductDetail =
                    Intent(this@ProductSearchActivity, ProductDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PRODUCT_KEY, product)
                intentProductDetail.putExtras(bundle)
                startActivity(intentProductDetail)
            }

        }
            ,
            object :OnChangeCartListener{
                override fun changeCartIcon(id: Int) {
                    TODO("Not yet implemented")
                }

            },"1",
             applicationContext)
        binding.productsSearchRecycler.adapter = productSearchAdapter
    }
}
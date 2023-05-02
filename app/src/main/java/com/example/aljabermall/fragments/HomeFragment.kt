package com.example.aljabermall.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.blueray.aljabermall.PRODUCT_KEY
import com.blueray.aljabermall.ProductDetailActivity
import com.example.aljabermall.*
import com.example.aljabermall.adapters.CateProductAdapter
import com.example.aljabermall.adapters.CateProductAdapter.Companion.CAT_ID
import com.example.aljabermall.adapters.OnChangeCartListener
import com.example.aljabermall.adapters.OnProductListener
import com.example.aljabermall.databinding.FragmentHomeBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.CategoriesItems
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.ProductItems
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.FavouriteViewModel
import com.example.aljabermall.viewmodels.ProductsViewModel


private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment<FragmentHomeBinding, ProductsViewModel>(), View.OnClickListener {

    override val viewModel by viewModels<ProductsViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private val favouriteViewModel by viewModels<FavouriteViewModel>()
    private var navController: NavController? = null
    private var cateProductsAdapter: CateProductAdapter? = null
    private val mToastDuration = 10000

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        if (!isNetWorkAvailable()) {
            binding.messageHome.show()
            binding.messageHome.text = getString(R.string.error)
        }

        setupCateProductsRecycler()

        HelperUtils.setDefaultLanguage(requireContext(),"ar")


        viewModel.getProductsCate().observe(viewLifecycleOwner) { results ->
            binding.progressHome.hide()
            binding.swipeToRefreshHome.isRefreshing = false
            when (results) {
                is NetworkResults.Success -> {
                    if (results.data.status.status == 1) {
                        val categories = results.data.categories_with_items.toMutableList()
                        Log.d("categories",categories.toString())
                        val cateHeader = CategoriesItems(
                            -1,
                            "",
                            "",
                            "",
                            -1,
                            emptyList(),
                        )
                        categories.add(0, cateHeader)
                        cateProductsAdapter?.submitList(categories)


                    } else {
                        binding.messageHome.text = results.data.status.msg
                        binding.messageHome.show()
                    }
                }
                is NetworkResults.Error -> {
                    binding.messageHome.apply {
                        show()
                        text = getString(R.string.error)
                    }
                    results.exception.printStackTrace()
                }
            }
        }

        favouriteViewModel.getAddToFavouriteMessage().observe(viewLifecycleOwner) { result ->
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                hideProgress()
                when (result) {
                    is NetworkResults.Success -> {
                        viewModel.retrieveProductCategories()
                        toast(result.data.status.msg)
                    }
                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                        toast(getString(R.string.error))
                    }
                }
            }
        }

        favouriteViewModel.getRemoveFavouriteMessage().observe(viewLifecycleOwner) { result ->
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                hideProgress()
                when (result) {
                    is NetworkResults.Success -> {
                        viewModel.retrieveProductCategories()
                        toast(result.data.status.msg)
                    }
                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                        toast(getString(R.string.error))
                    }
                }
            }
        }

        cartViewModel.getAddToCartMessage().observe(viewLifecycleOwner) { result ->
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

        viewModel.retrieveProductCategories()

        binding.swipeToRefreshHome.setOnRefreshListener {
            viewModel.retrieveProductCategories()
        }

        binding.drawerBtn.setOnClickListener(this)
        binding.cartIcon.setOnClickListener(this)
        binding.searchProductsBtn.setOnClickListener(this)
    }


    private fun mDisplayToast(toast: Toast){
        Thread{
            for(i in 1..mToastDuration/2000){
                toast.show()
                Thread.sleep(2000)
                toast.cancel()
            }
        }.start()

    }

    private fun setupCateProductsRecycler() {
        cateProductsAdapter = CateProductAdapter(object : OnProductListener {
            override fun addToCart(price: Double?, pid: Int?, quantity: String) {
                showProgress()
                cartViewModel.addToCart(price!!, pid.toString(), quantity)
                Log.d(TAG, "addToCart: " + pid.toString())
            }


            override fun addToFavourite(pid: Int) {
                Log.i(TAG, "addToFavourite: $pid")
                showProgress()
                favouriteViewModel.addToFavourite(pid.toString())
            }

            override fun removeFromFavourite(favId: Int) {
                Log.i(TAG, "removeFromFavourite: $favId")
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


        }, object : OnChangeCartListener{
            override fun changeCartIcon(id: Int) {
//                binding.cartIcon.setColorFilter(Color.WHITE)
//                binding.cartIcon.setImageResource(R.drawable.notifycart)

                binding.notifyBell.visibility = View.VISIBLE
            }

        }, applicationContext()) { bundle ->

            val intentCategories = Intent(context, CategoriesActivity::class.java)
            bundle.putInt(CATE_ID, CAT_ID)
            intentCategories.putExtras(bundle)
            startActivity(intentCategories)
            //category click listener
//            val intentProductDetail = Intent(mContext, CategoriesActivity::class.java)
            // get the CATE_id
//            bundle.putInt(CATE_ID, bundle.)
//            intentProductDetail.putExtras(bundle)
//            startActivity(intentProductDetail)
        }
        binding.notifyBell.visibility = View.GONE


        binding.productsCatRecycler.setHasFixedSize(true)
        binding.productsCatRecycler.adapter = cateProductsAdapter
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.drawer_btn -> (requireActivity() as HomeActivity).openDrawer()

            R.id.cart_icon ->
                if (HelperUtils.getUID(mContext) == "0"){
                    HelperUtils.openLoginActivity(mContext)
                }else{
                    navController?.navigate(R.id.action_homeFragment_to_cartFragment)

                }

            R.id.search_products_btn -> {
                val intentSearchActivity = Intent(mContext, ProductSearchActivity::class.java)
                startActivity(intentSearchActivity)
            }
        }
    }
}
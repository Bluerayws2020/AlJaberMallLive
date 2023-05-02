package com.blueray.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import coil.load
import coil.size.Scale

import com.example.aljabermall.R
import com.example.aljabermall.adapters.GenericAdapter
import com.example.aljabermall.databinding.ActivityProductDetailBinding
import com.example.aljabermall.databinding.ImageProudectBinding
import com.example.aljabermall.databinding.ItemProductRelatedBinding
import com.example.aljabermall.fragments.ProgressDialogFragment
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.ProductItems
import com.example.aljabermall.models.ProudectImageModel
import com.example.aljabermall.models.RelatedProductItemss
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.FavouriteViewModel
import com.example.aljabermall.viewmodels.ProductsViewModel
import java.util.*

const val PRODUCT_KEY = "product"

class ProductDetailActivity : AppCompatActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivityProductDetailBinding
    private val productVM by viewModels<ProductsViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private val favouriteVM by viewModels<FavouriteViewModel>()
    private var progressDialog = ProgressDialogFragment()
    private var product: ProductItems? = null
    private var language = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = getString(R.string.item_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        language = HelperUtils.getLang(this)
        HelperUtils.setDefaultLanguage(this,"ar")

        showProgress()

        val bundle = intent.extras

        product = bundle?.getParcelable(PRODUCT_KEY)

//showProductDetail(product)

        productVM.getRelatedProducts().observe(this) { result ->
            dismissDialog()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {

//related proudect
                        setupProductRecycler(result.data.items.related)
                        showProductDetail(result.data.items)


                        if (result.data.items.images.size == 0) {

                            binding.productImageRec.hide()
                            binding.imageHolder.show()

                        }else {
                            binding.productImageRec.show()
                            binding.imageHolder.hide()

                        }

                        setuImageRecycler(result.data.items.images)

                    } else {
                        binding.relatedMessage.show()
                        binding.relatedProductsRecycler.hide()

                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.relatedMessage.show()
                    binding.relatedProductsRecycler.hide()
                    Log.d("datas",result.exception.message.toString())

                }
            }
        }
        productVM.retrieveRelatedProducts(product?.id.toString())

        favouriteVM.getAddToFavouriteMessage().observe(this) { result ->
            dismissDialog()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1)
                        product?.favorite_status = result.data.status.status.toInt()
                    toast(result.data.status.msg)
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    toast(result.exception.toString())

                }
            }
        }

        favouriteVM.getRemoveFavouriteMessage().observe(this) { result ->
            dismissDialog()
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

        cartViewModel.getAddToCartMessage().observe(this) { result ->
            dismissDialog()
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


        //retrieve related products by product id
        Log.d("sadproductVM",product?.id.toString())
        binding.addQuantity.setOnClickListener(this)
        binding.removeQuantity.setOnClickListener(this)
        binding.addToCartBtn.setOnClickListener(this)
        binding.favouriteCheckbox.setOnCheckedChangeListener(this)
    }

    private fun dismissDialog() {
        if (progressDialog.isAdded)
            progressDialog.dismiss()
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showProductDetail(product: RelatedProductItemss?) {
        if (language == "ar")
            binding.productTitle.text = product?.item_name_ar
        else
            binding.productTitle.text = product?.item_name_ar

        binding.productPrice.text = product?.item_price.toString()  + "JD"

        if (product?.favorite_status == 1) {
            binding.favouriteCheckbox.isChecked = true
        }else{
            binding.favouriteCheckbox.isChecked = false
        }

        if (product?.stock == 0.0000 && product.always_in_stock == 0) {

            binding.outOfStocks.show()
            binding.linearLayout4.hide()
        }else{
            binding.outOfStocks.hide()
            binding.linearLayout4.show()
        }

    }

    private fun setupProductRecycler(productItems: List<ProductItems>) {
        val relatedProductsAdapter = object :
            GenericAdapter<ProductItems, ItemProductRelatedBinding>(productItems) {
            override fun onBindData(model: ProductItems?, dataBinding: ItemProductRelatedBinding?) {
                dataBinding?.productImage?.load(HelperUtils.BASE_URL  + model?.item_image) {
                    placeholder(R.drawable.image)
                    error(R.drawable.image)
                    scale(Scale.FIT)


                }
            }

            override fun onItemClick(model: ProductItems?) {
                val intentProductDetail =
                    Intent(this@ProductDetailActivity, ProductDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PRODUCT_KEY, model)
                intentProductDetail.putExtras(bundle)
                startActivity(intentProductDetail)
            }

            override fun getViewBinding(viewGroup: ViewGroup?): ItemProductRelatedBinding {
                return ItemProductRelatedBinding.inflate(
                    LayoutInflater.from(viewGroup?.context),
                    viewGroup,
                    false
                )
            }

        }

        binding.relatedProductsRecycler.adapter = relatedProductsAdapter
    }




//Image Recycler

    private fun setuImageRecycler(productItems: List<ProudectImageModel>) {


        val imag_adabter = object :
            GenericAdapter<ProudectImageModel, ImageProudectBinding>(productItems) {
            override fun onBindData(model: ProudectImageModel?, dataBinding: ImageProudectBinding?) {
                dataBinding?.productImage?.load(HelperUtils.BASE_URL  + model?.proudectdetails_image) {
                    placeholder(R.drawable.image)
                    error(R.drawable.image)
                    scale(Scale.FIT)

                    Log.d("IMMMAGA", model?.proudectdetails_image!!)
                }
            }

            override fun onItemClick(model: ProudectImageModel?) {
                val intentProductDetail =
                    Intent(this@ProductDetailActivity, ProductDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(PRODUCT_KEY, model)
                intentProductDetail.putExtras(bundle)
                startActivity(intentProductDetail)
            }

            override fun getViewBinding(viewGroup: ViewGroup?): ImageProudectBinding {
                return ImageProudectBinding.inflate(
                    LayoutInflater.from(viewGroup?.context),
                    viewGroup,
                    false
                )
            }

        }

        binding.productImageRec.adapter = imag_adabter
    }


    override fun onClick(v: View?) {
        var quantity = binding.productQuantityCounter.text.toString().toDouble()
        when (v?.id) {
            R.id.add_quantity -> {
//                if (product?.unit_type == 1) {
//                    quantity += 0.5
//                    binding.productQuantityCounter.text = quantity.toString()
////                    Toast(this,)
//                } else {
                quantity += 1.0
                binding.productQuantityCounter.text = quantity.toString()
                binding.addToCartBtn.show()

//                }

            }
            R.id.remove_quantity -> {
//                if (product?.unit_type == 1) {
//                    if (quantity > 0.0) {
//                        quantity -= 0.5
//                    }
//                    binding.productQuantityCounter.text = quantity.toString()
//                } else {


                if (quantity == 0.0){
                }else{

                    quantity -= 1.0

                }

                binding.productQuantityCounter.text = quantity.toString()


            }
            R.id.add_to_cart_btn -> {
                showProgress()
                if (HelperUtils.getUID(this) == "0"){
HelperUtils.openLoginActivity(this)
                }else {
                    cartViewModel.addToCart(
                        product?.item_price!!,
                        product!!.category_id.toString(),
                        quantity.toString()
                    )
                }
                }
        }
    }

    private fun showProgress() {
        progressDialog.show(supportFragmentManager, "product_detail")
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView?.isPressed == true) {
            if (!HelperUtils.isGuest(this)) {
                showProgress()
                if (isChecked) {
                    favouriteVM.addToFavourite(product?.id.toString())
                } else {
                    favouriteVM.removeFavourite(product?.favorite_status.toString())
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}
package com.example.aljabermall

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.example.aljabermall.adapters.GenericAdapter
import com.example.aljabermall.databinding.ActivityOrderProductsBinding
import com.example.aljabermall.databinding.ItemOrderProductBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.SalesOperations
import java.util.*

class OrderProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderProductsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        HelperUtils.setDefaultLanguage(this,"ar")

        supportActionBar?.title = getString(R.string.order_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productList = intent.extras?.getParcelableArrayList<SalesOperations>("order_items")

        setupItemsRecycler(productList)
    }

    private fun setupItemsRecycler(productList: ArrayList<SalesOperations>?) {
        val orderItemAdapter =
            object : GenericAdapter<SalesOperations, ItemOrderProductBinding>(productList!!) {
                override fun onBindData(
                    model: SalesOperations?,
                    dataBinding: ItemOrderProductBinding?
                ) {
                    dataBinding?.productImage?.load(HelperUtils.BASE_URL + model?.item_image) {
                        placeholder(R.drawable.image)
                        error(R.drawable.image)
                        scale(Scale.FIT)
                        crossfade(true)
                    }
                    dataBinding?.quantity?.text = model?.item_quantity.toString()
                    dataBinding?.totalPrice?.text = model?.total_unit_price.toString()
                }

                override fun onItemClick(model: SalesOperations?) {
                    //Not yet implemented
                }

                override fun getViewBinding(viewGroup: ViewGroup?): ItemOrderProductBinding {
                    return ItemOrderProductBinding.inflate(
                        LayoutInflater.from(viewGroup?.context),
                        viewGroup,
                        false
                    )
                }

            }
        binding.orderItemsRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.orderItemsRecycler.adapter = orderItemAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}
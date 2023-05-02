package com.example.aljabermall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.aljabermall.adapters.OrderAdapter
import com.example.aljabermall.databinding.ActivityOrdersBinding
import com.example.aljabermall.helpers.ContextWrapper
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.Invoices
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.CartViewModel
import com.example.aljabermall.viewmodels.LocationsViewModel
import java.util.*
import kotlin.collections.ArrayList

class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private val cartVM by viewModels<CartViewModel>()
    private val locationVM by viewModels<LocationsViewModel>()

    private var locationId: Int ?= null

    companion object {
        var city: String? = ""
        var POSITION: Int? = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        supportActionBar?.title = getString(R.string.my_orders)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        HelperUtils.setDefaultLanguage(this,"ar")


        if (!HelperUtils.isNetWorkAvailable(this)) {
            binding.messageOrders.show()
            binding.messageOrders.text = getString(R.string.error)
        }

        cartVM.retriveInvoiceInfo()


//        location_id
//        val locationId = intent.extras?.getString("location_id")

//        locationVM.getAddressDetails(locationId.toString())
        Log.d("Location Id = ", "onCreate: $locationId")


        cartVM.getInvoices().observe(this) { result ->
            binding.progressBarOrders.hide()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {
                        setupOrders(result.data.invoices)

                    } else {
                        binding.messageOrders.show()
                    }
                }
                is NetworkResults.Error -> {
                    binding.messageOrders.show()
                    result.exception.printStackTrace()
                }
            }
        }
    }

    private fun setupOrders(listInvoices: List<Invoices>) {


        val orderAdapter = OrderAdapter(listInvoices
        ) { pos ->
            val intentOrderDetail = Intent(this, OrderProductsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelableArrayList(
                "order_items",
                ArrayList(listInvoices[pos].sale_operations)
            )
            intentOrderDetail.putExtras(bundle)
            startActivity(intentOrderDetail)
        }

        binding.ordersRecycler.adapter = orderAdapter
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

//    getAddressDetailsMessage

    private fun getAddressDetails(){


    }


}
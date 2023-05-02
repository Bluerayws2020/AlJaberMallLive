package com.example.aljabermall.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.example.aljabermall.HomeActivity
import com.example.aljabermall.OrderProductsActivity
import com.example.aljabermall.R
import com.example.aljabermall.adapters.GenericAdapter
import com.example.aljabermall.databinding.ActivityOrderDetalisBinding
import com.example.aljabermall.databinding.ActivityOrderProductsBinding
import com.example.aljabermall.databinding.FragmentCartBinding
import com.example.aljabermall.databinding.ItemOrderProductBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.SalesOperations
import com.example.aljabermall.viewmodels.CartViewModel
import java.util.ArrayList


class orderFragment: AppCompatActivity(){
    private val viewModel by viewModels<CartViewModel>()
    private lateinit var binding: ActivityOrderDetalisBinding
    private var navController: NavController? = null
var arrCartItem:List<SalesOperations>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderDetalisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getOrderDetails(CartFragment.orderId)

        orderCallApi()
        HelperUtils.setDefaultLanguage(this,"ar")


binding.moreDetail.setOnClickListener{

    val intentOrderDetail = Intent(this, OrderProductsActivity::class.java)
    val bundle = Bundle()
    bundle.putParcelableArrayList(
        "order_items",ArrayList(arrCartItem)
    )

    intentOrderDetail.putExtras(bundle)
    startActivity(intentOrderDetail)


}


        binding.doneBtn.setOnClickListener{
//                        navController?.popBackStack()
            val intent  = Intent(this,HomeActivity::class.java)


            startActivity(intent)


        }

    }



    private fun setupItemsRecycler(productList: List<SalesOperations>) {
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
        binding.itemRec.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.itemRec.adapter = orderItemAdapter
    }


    fun orderCallApi(){

        viewModel.getOrderDetails().observe(this) { result ->
          binding.progressbr.show()
          when (result) {
              is NetworkResults.Success -> {
                  binding.progressbr.hide()
//                  Toast.makeText(this,result.data.status.toString(),Toast.LENGTH_LONG).show()

                  if (result.data.status.status == 1) {
//                      Toast.makeText(this,result.data.status.toString(),Toast.LENGTH_LONG).show()
                      val item = result.data.invoices

binding.orderNo.text  =  "طلب رقم : ${item.order_id}"
 binding.paymentDate.text    = item.date

                      binding.paymentStatus.text= item.state
                      binding.totalPrice.text = item.total_order_price + "\t" + "JD"




                      binding.itemsNo.text= result.data.invoices.items.size.toString()
                      setupItemsRecycler(item.items)
                      arrCartItem = item.items

                      if (item.location == "" || item.location.isNullOrEmpty())
                          binding.shippingAddress.text= "استلام من الموقع"
                      else
                          binding.shippingAddress.text= item.location


                      if (item.phone_number == "" ) {
                          binding.phoneNumber.text = "-"
                          binding.phoneNumber.hide()
                      }
                      else {
                          binding.phoneNumber.text = item.phone_number
                      }

                      if (item.shipping_fees?.amount == "")
                          binding.shippingFees.text = "-"
                      else
                          binding.shippingFees .text= item.shipping_fees?.amount



                  } else {
//                      binding.messageOrders.show()
                      binding.progressbr.hide()
//                      Toast.makeText(this,result.data.status.toString(),Toast.LENGTH_LONG).show()


                  }
              }
              is NetworkResults.Error -> {
//                  binding.messageOrders.show()
                  Toast.makeText(this,result.exception.toString(),Toast.LENGTH_LONG).show()

                  result.exception.printStackTrace()
                  binding.progressbr.hide()

              }
          }
      }
  }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,HomeActivity::class.java))
    }
  }




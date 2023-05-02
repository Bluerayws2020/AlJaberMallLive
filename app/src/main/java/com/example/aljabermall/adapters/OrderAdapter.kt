package com.example.aljabermall.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aljabermall.OrdersActivity
import com.example.aljabermall.R
import com.example.aljabermall.api.ApiClient
import com.example.aljabermall.databinding.ItemOrderBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.Invoices
import com.example.aljabermall.models.InvoicesModel
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.UserLocationsModel
import com.example.aljabermall.viewmodels.LocationsViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private val listInvoices: List<Invoices>,
    private val onOrderClick: (Int) -> Unit,
) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

    private var mContext: Context? = null
    private var profileId: Int ?= null


        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context
    }

    inner class OrderHolder(binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        val orderNo = binding.orderNo
        val itemNo = binding.itemsNo
        val totalPrice = binding.totalPrice
        val paymentStatus = binding.paymentStatus
        val shippingAddress = binding.shippingAddress


        //new
        val date = binding.paymentDate
        private val moreDetail = binding.moreDetail

        init {
            moreDetail.setOnClickListener {
                onOrderClick(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        val order = listInvoices[position]

        OrdersActivity.POSITION = position

        holder.orderNo.text = mContext?.getString(R.string.order_no, order.order_id)
        holder.totalPrice.text = order.total_price.toString()
        holder.itemNo.text = order.sale_operations.size.toString()
        holder.paymentStatus.text = mContext?.getString(R.string.cash)

        profileId = order.order_id

//        Log.d("prod nums = ", "onBindViewHolder: ${order.sale_operations.size}")

        /*if (order.payment_status == 1)
            holder.paymentStatus.text = mContext?.getString(R.string.cash)
        else
            holder.paymentStatus.text = mContext?.getString(R.string.online)*/
//        if(holder.shippingAddress != null){ holder.shippingAddress.text = "${order.city}\n${order.area}\n${order.street}" }
//        else { holder.shippingAddress.text = "-" }

        val dfs = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val nowAsString = dfs.format(Date())

        holder.date.text = order.inVodate
        holder.date.text = nowAsString.toString()


        val language = "ar"
        val userIdBody = HelperUtils.getUID(mContext).toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val addressDetails =
            ApiClient.retrofitService
                .getAddressInOrders(languageBody , userIdBody)



        addressDetails.enqueue(

            object : Callback<InvoicesModel> {
                override fun onResponse(
                    call: Call<InvoicesModel>,
                    response: Response<InvoicesModel>
                ) {
                    if (response.body()?.status?.status == 1) {

//                        holder.shippingAddress.text = "${response.body()!!.invoices [holder.bindingAdapterPosition].billing_information.city}\t - " +
//                                "${ response.body()!!.invoices [holder.bindingAdapterPosition].billing_information.given_name}\t - " +
//                                "${response.body()!!.invoices [holder.bindingAdapterPosition].billing_information.locality}\t - " +
//                                response.body()!!.invoices [holder.bindingAdapterPosition].billing_information.address_line1

//                        Log.d("aya = ", "onResponse: " + response.body()!!.invoices[holder.bindingAdapterPosition].billing_information.toString())
                    }

                }

                override fun onFailure(call: Call<InvoicesModel>, t: Throwable) {
                    Toast.makeText(mContext, t.message.toString(), Toast.LENGTH_LONG).show()
                }

            }


        )
    }

    override fun getItemCount(): Int {
        return listInvoices.size
    }

}
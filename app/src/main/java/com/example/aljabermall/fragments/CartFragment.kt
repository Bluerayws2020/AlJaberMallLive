package com.example.aljabermall.fragments

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.aljabermall.AddressCreationActivity
import com.example.aljabermall.HomeActivity
import com.example.aljabermall.PaymentActivity
import com.example.aljabermall.R
import com.example.aljabermall.adapters.CartAdapter
import com.example.aljabermall.adapters.OnCartListener
import com.example.aljabermall.api.RefreshCart
import com.example.aljabermall.databinding.FragmentCartBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.CityItem
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.CartViewModel
import java.text.DecimalFormat

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(),
    AdapterView.OnItemSelectedListener, View.OnClickListener,RefreshCart {
    override val viewModel by viewModels<CartViewModel>()
    private var navController: NavController? = null
    private var cartAdapter: CartAdapter? = null
    private var paymentStatus = 0
    private var receiptStatus = 0
    private var locationId = ""
    private var promoCode = ""
    private var numOfOrders=0

    var cityId = ""
    var areaId = ""


    var flagCheckPromo = 0
    private val cityName = mutableListOf<String>()
    private val areaName = mutableListOf<String>()

    var mpopup: PopupWindow? = null
    var orderIdafterCheckOut = ""
var couponId:String? = null
    companion object {

        var orderId = ""
        var dismiss = ""
    }
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.includeTab.tabTitle.text = getString(R.string.review_cart)
        binding.includeTab.backButton.setOnClickListener(this)
        orderIdafterCheckOut = orderId
        showProgress()
        setupRecycler()
        updateCartForTakeAway()
        HelperUtils.setDefaultLanguage(requireContext(),"ar")

//        deefalut Value
        binding.couponView.hide()
        binding.couponAdd.show()
        binding.couponAdd.setOnClickListener{
            binding.couponAdd.hide()
            binding.couponView.show()
        }

        viewModel.getCart().observe(viewLifecycleOwner) { result ->
            hideProgress()
            binding.swipeToRefresh.isRefreshing = false
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 1) {
                        val df = DecimalFormat("#.##")
                        binding.total.text =
                            df.format(result.data.user_cart.total_order_price.toDouble())
                        binding.swipeToRefresh.isRefreshing = false


                        binding.delivery.text =
                            df.format(result.data.user_cart.shiping_fees.toDouble())

//                        binding.total.text =
//                            df.format(result.data.user_cart.total_order_price.toDouble() + result.data.user_cart.shiping_fees.toDouble())


                        if (result.data.user_cart.shiping_fees.toInt() != 0) {
                            binding.subtotal.text =
                                df.format(result.data.user_cart.total_order_price.toDouble() - result.data.user_cart.shiping_fees.toDouble())

                            binding.locationNameCart.text = " تم تحديد الموقع"
                        }
                        else {

                            binding.subtotal.text =
                                df.format(result.data.user_cart.total_order_price.toDouble() - result.data.user_cart.shiping_fees.toDouble())
                            binding.locationNameCart.text = "لم يتم تحديد الموقع"

}






                        if (result.data.user_cart.coupons.isNullOrEmpty() == false){
                        val prcode = result.data.user_cart.coupons[0].code.toString()
                        binding.promoCodeEt.setText( "$prcode")
                        binding.promoCodeEt.isEnabled = false
                        binding.applyPromoCodeBtn.hide()
                        binding.removeItemPromoCodeBtn.show()

                            couponId = result.data.user_cart.coupons[0].coupon_id.toString()
                        }

                        if (result.data.user_cart.shiping_fees.isNullOrEmpty() == false){
                            binding.locationNameCart.text = "تم تحديد الموقع"
                            binding.imgdelivery.setImageResource(R.drawable.chevron_down)
                        }else{
                            binding.locationNameCart.text = "لم يتم تحديد الموقع"
                            binding.imgdelivery.setImageResource(R.drawable.chevron_down)

                        }

                        cartAdapter?.submitList(result.data.user_cart.cart_items)
                        cartAdapter?.notifyDataSetChanged()
                        numOfOrders=result.data.user_cart.cart_items.size
                        orderId = result.data.user_cart.order_id




                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
//                    requireActivity().onBackPressed()
                    Log.d("CARTITEMERROR", result.exception.toString())
//                    toast(getString(R.string.error))
                }
            }
        }

//        viewModel.retrieveCart()

        viewModel.getUpdateCartMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
//                    toast(result.data.status.msg)
                    viewModel.retrieveCart()
                }
                is NetworkResults.Error -> {
//                    toast(getString(R.string.error))
                    result.exception.printStackTrace()
                }
            }
        }
        viewModel.getDeleteFromCartMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
//                    toast(result.data.status.msg)
                    if((numOfOrders-1)==0){
                        requireActivity().onBackPressed()
                    }
                    viewModel.retrieveCart()
                }
                is NetworkResults.Error -> {
//                    toast(getString(R.string.error))

                    result.exception.printStackTrace()
                }
            }
        }

        viewModel.getPromoCodeResponse().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {
                      binding.applyPromoCodeBtn.hide()
                        binding.promoCodeEt.isEnabled = false
                        binding.removeItemPromoCodeBtn.show()


                        viewModel.retrieveCart()
                        toast(result.data.msg.msg.toString())

                    } else
                        toast(result.data.msg.msg.toString())

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
//                    toast(getString(R.string.error))
                }
            }
        }

        viewModel.getRemovePromoCode().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {
//                        add text
//                        binding.promoCodeEt.text = getString(R.string.promo_code)
                        binding.applyPromoCodeBtn.show()
                        binding.promoCodeEt.isEnabled = true
                        binding.removeItemPromoCodeBtn.hide()
                        viewModel.retrieveCart()
                        toast(result.data.msg.msg.toString())

                    } else
                        toast(result.data.msg.msg.toString())

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
//                    toast(getString(R.string.error))
                }
            }
        }



        viewModel.getAddPhoneMessage().observe(viewLifecycleOwner) { result ->
            hideProgress()
            when (result) {
                is NetworkResults.Success -> {
                    toast(result.data.msg)
                }
                is NetworkResults.Error -> {
//                    toast(getString(R.string.error))
                    result.exception.printStackTrace()
                }
            }
        }

        viewModel.getCartCheckoutMessage().observe(viewLifecycleOwner) { result ->
            binding.progressCheckout.hide()
            binding.placeOrder.show()
            when (result) {
                is NetworkResults.Success -> {
                    toast(result.data.status.msg)
                    if (result.data.status.status == 1) {
//                        navController?.popBackStack()

                        val intentProductDetail =
                            Intent(requireContext(), orderFragment::class.java)
                        intentProductDetail.putExtra("orderid", orderId)
                        startActivity(intentProductDetail)



                        binding.spinnerPayments.setSelection(0)
                        binding.spinnerReceipt.setSelection(0)
                        binding.couponAdd.show()
                        binding.couponView.hide()
                    } else {
                        val intentProductDetail =
                            Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intentProductDetail)

                    }
                }
                is NetworkResults.Error -> {
//                    toast(getString(R.string.error))
                    result.exception.printStackTrace()
                }
            }
        }

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.retrieveCart()
        }

        binding.placeOrder.setOnClickListener(this)
        binding.deliveryCard.setOnClickListener(this)
        binding.applyPromoCodeBtn.setOnClickListener(this)
        binding.removeItemPromoCodeBtn.setOnClickListener(this)
        binding.spinnerReceipt.onItemSelectedListener = this
        binding.spinnerPayments.onItemSelectedListener = this

    }

    private fun isCartValid(): Boolean {
        var status = true
        var message = ""

        if (paymentStatus == 0) {
            status = false
            message += getString(R.string.payment_message) + "\n"
        }

        if (receiptStatus == 0) {
            status = false
            message += getString(R.string.receipt_message) + "\n"
        }

//        if (receiptStatus == 2 && locationId.isEmpty()) {
//            status = false
//            message += getString(R.string.select_location_message)
//        }
//github.com/Bluerayws2020//AlJaberMall
        if (message.trim().isNotEmpty())
            Toast.makeText(mContext, message.trim(), Toast.LENGTH_LONG).show()

        return status
    }

    private fun setupRecycler() {
        cartAdapter = CartAdapter(object : OnCartListener {
            override fun removeFromCart(pid: Int,position: Int) {
                showProgress()


                viewModel.deleteFromCart(pid.toString())

            }

            override fun updateCartItem(pid: Int, quantity: Int, order_idBody: String) {


                if (quantity == 1){

                    toast("لا يمكن ان يكون العدد اقل من وحد")

                }else {
                    showProgress()

                    viewModel.updateCart(pid.toString(), quantity.toString(), order_idBody.toString())

                }
            }

        })

        binding.cartItemsRecycler.adapter = cartAdapter

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_payments -> {
                //make payment method one type (cash)
                //1 for cash & 2 for online payment

                paymentStatus = position

//                if (paymentStatus == 2){
////    online
//                    val intentProductDetail =
//                        Intent(requireContext(), PaymentActivity::class.java)
//                    val bundle = Bundle()
//                    bundle.putString("orderid", this.orderId)
//                    intentProductDetail.putExtras(bundle)
//                    startActivity(intentProductDetail)
//
//                }
//                else
//                {
//                    // cash
//                }


            }
            R.id.spinner_receipt -> {
                //1 for pickup & 2 for delivery

                receiptStatus = position
                if (receiptStatus == 2) {
//                    binding.deliveryLabel.show()
//                    binding.deliveryCard.show()
//                    binding.deliveryLine.show()
//                    viewModel.retrieveCart()

                    binding.deliveryCard.show()

                } else {

                    viewModel.updateTakeAwayCart(
                        orderId, "0"
                    )



                    binding.deliveryLabel.hide()
                    binding.deliveryCard.hide()
                    binding.deliveryLine.hide()
                    viewModel.retrieveCart()
//                }
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        when (parent?.id) {
            R.id.spinner_payments -> {
                paymentStatus = 0
            }
            R.id.spinner_receipt -> {
                receiptStatus = 0
            }
        }
    }

    private val locationResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                locationId = result.data?.getStringExtra("location_id")!!
                val locationName = result.data?.getStringExtra("location_name")
                binding.locationNameCart.text = locationName
            }
        }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.place_order -> {
                if (isCartValid()) {
                    if (!HelperUtils.isGuest(mContext)) {
//                        binding.placeOrder.hide()
//                        binding.progressCheckout.show()
                        if (paymentStatus == 2) {
                            val intentProductDetail =
                                Intent(requireContext(), PaymentActivity::class.java)

                            startActivity(intentProductDetail)
                        } else {
                            viewModel.cartCheckout(
                                orderId.toInt()
                            )





                        }


                    }
                }
            }
            R.id.delivery_card -> {
//                if (!HelperUtils.isGuest(mContext)) {
//                    val intentLocation = Intent(mContext, AddressCreationActivity::class.java)
//                    locationResults.launch(intentLocation)
//                }

//                UserInformationDialog(getString(R.string.user_information)) {
////                    showProgress()
//
//                }.show(childFragmentManager, "user_information")

                diliveryPop()
                viewModel.retrieveCart()
            }
            R.id.apply_promo_code_btn -> {


                    HelperUtils.hideKeyBoard(requireActivity())
                    if(!HelperUtils.isGuest(mContext)) {
                        showProgress()
                        promoCode = binding.promoCodeEt.text.toString()

                        viewModel.checkPromoCode(orderId,promoCode)






                }





            }


            R.id.back_button -> {
                navController?.popBackStack()
            }
            R.id.remove_item_promo_code_btn->{
                HelperUtils.hideKeyBoard(requireActivity())
                if(!HelperUtils.isGuest(mContext)) {
                    showProgress()
                    promoCode = binding.promoCodeEt.text.toString()
                    viewModel.removecheckPromoCode(orderId, couponId!!)
                }
            }
        }
    }



    fun updateCartForTakeAway() {
        viewModel.getTakeAwayInfo().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 1) {

                        Toast.makeText(context, result.data.status.msg, Toast.LENGTH_SHORT).show()

                        viewModel.retrieveCart()

                    } else {
                        Toast.makeText(context, result.data.status.msg, Toast.LENGTH_SHORT).show()
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
//                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun OnCloseDialog(obj: String?) {
        if (obj == "1") {
            viewModel.retrieveCart()

        }
        toast("Dismiss")

    }
    fun diliveryPop() {
        val popUpView: View = layoutInflater.inflate(
            R.layout.informationcutomer,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, true
        ) // Creation of popup

        mpopup!!.setAnimationStyle(android.R.style.Animation_Dialog)
        mpopup!!.showAtLocation(popUpView, Gravity.TOP , 0, 0) // Displaying popup



        val fname = popUpView.findViewById<EditText>(R.id.firstName) as EditText
        val lname = popUpView.findViewById<EditText>(R.id.lastName) as EditText
        val phone = popUpView.findViewById<EditText>(R.id.phone_number_et) as EditText
        val citySpinner = popUpView.findViewById<Spinner>(R.id.citySpinner) as Spinner
        val areaSpinner = popUpView.findViewById<Spinner>(R.id.areaSpinner) as Spinner
        val pd = popUpView.findViewById<ProgressBar>(R.id.pd) as ProgressBar
        val dismiss = popUpView.findViewById<RelativeLayout>(R.id.dismis_view) as RelativeLayout
        val send = popUpView.findViewById<Button>(R.id.send) as Button

        spinnerSetup(citySpinner,pd,areaSpinner)


dismiss.setOnClickListener{
    mpopup?.dismiss()

}
        send.setOnClickListener{
            binding.progressCheckout.show()
            pd.show()
            send.hide()
            updateCartForDeleivery(send,pd)

            viewModel.updateDeliveryCart(
                orderId,areaId,
                fname.text.toString(),lname.text.toString(),
                phone.text.toString())
        }
    }


    fun spinnerSetup(spinner: Spinner, pd: ProgressBar,areaSpinner:Spinner){
        viewModel.cityData()

        viewModel.gitCityData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
cityName.clear()

                    if (result.data.status?.status == 1){
                        pd.hide()
                        spinnerAppenData(result.data.cityItem,spinner,pd,areaSpinner)
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }


    fun spinnerAppenData (station:List<CityItem>, spinner: Spinner,pd:ProgressBar,area:Spinner){

        station.forEach {
            cityName.addAll(listOf(it.name.toString()))
        }

        if (spinner != null) {
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, cityName)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    cityId =   station[position].tid.toString()
                    viewModel.areaData(cityId)
                   pd.show()
                    getArea(pd,area)

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }




    }


    fun getArea(pd:ProgressBar,sp:Spinner){
        viewModel.getAreaData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {


                    areaName.clear()
                    if (result.data.status?.status == 1){
                     pd.hide()
                        spinnerAppenDataForArea(result.data.cityItem, sp)
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }
    }


    fun spinnerAppenDataForArea (station:List<CityItem>, spinner: Spinner){

        station.forEach {
            areaName.addAll(listOf(it.name.toString()))
        }

        if (spinner != null) {
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, areaName)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    areaId =   station[position].tid.toString()
binding.deliveryLabel.text = station[position].name.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }




    }

    fun getcart(fname:String,lname:String,phone:String){
        viewModel.getCart().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 1) {


//                    Toast.makeText(context,result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()



                    } else {
                        Toast.makeText(context, result.data.status.msg.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
//                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }}

    fun updateCartForDeleivery(btn:Button,pd: ProgressBar){

        viewModel.getDeliveryInfo().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.progressCheckout.hide()

                    if (result.data.status.status == 1) {

                        binding.progressCheckout.hide()

                        pd.hide()

                        btn.show()

mpopup?.dismiss()

viewModel.retrieveCart()


                    } else {
                        pd.hide()

                        binding.progressCheckout.hide()
                        btn.show()
                        Toast.makeText(context, result.data.status.msg, Toast.LENGTH_SHORT).show()
                    }
                    binding.progressCheckout.hide()

                }
                is NetworkResults.Error -> {
                    pd.hide()

                    binding.progressCheckout.hide()
                    btn.show()
                    result.exception.printStackTrace()
//                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }}

}
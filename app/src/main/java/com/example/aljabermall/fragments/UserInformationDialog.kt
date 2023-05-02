package com.example.aljabermall .fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.aljabermall.R
import com.example.aljabermall.adapters.CartAdapter
import com.example.aljabermall.adapters.OnProductListener
import com.example.aljabermall.api.RefreshCart
import com.example.aljabermall.databinding.InformationcutomerBinding
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.helpers.ViewUtils.hide
import com.example.aljabermall.helpers.ViewUtils.show
import com.example.aljabermall.models.CityItem
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.viewmodels.CartViewModel


class UserInformationDialog(order_Id:String,
                            private val onPhoneSubmitListener: (String) -> Unit)
    : DialogFragment() {
    var mListener: RefreshCart? = null


    companion object{
        var flag: Boolean ?= false
    }
init {
  val  listener: RefreshCart?= null
    mListener = listener

}


    private var binding: InformationcutomerBinding? = null
    private val viewModel by viewModels<CartViewModel>()
    private var cartAdapter: CartAdapter? = null
    var orderId = order_Id

    var cityId = ""
    var areaId = ""

    private val cityName = mutableListOf<String>()
    private val areaName = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InformationcutomerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveCart()
        HelperUtils.setDefaultLanguage(requireContext(),"ar")

        val arrayOfCity : ArrayList<String>  = arrayListOf()
        val arrayOfCountry : ArrayList<String>  = arrayListOf()


        var countryId : String ?= ""
        var cityId : String ?= ""

getArea()


        updateCartForDeleivery()



        binding?.send?.setOnClickListener {
binding!!.pd.show()

        getcart()



        }

viewModel.retrieveCart()


        spinnerSetup(binding!!.citySpinner, binding!!.pd)



    }




    fun spinnerSetup(spinner: Spinner, pd: ProgressBar){
        viewModel.cityData()

        viewModel.gitCityData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status?.status == 1){
                        pd.hide()
                        spinnerAppenData(result.data.cityItem,spinner)
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }


    fun spinnerAppenData (station:List<CityItem>, spinner: Spinner){

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
                    binding!!.pd.show()


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }




    }


fun getArea(){
    viewModel.getAreaData().observe(viewLifecycleOwner) { result ->
        when (result) {
            is NetworkResults.Success -> {

                if (result.data.status?.status == 1){
                    binding!!.pd.hide()
                    spinnerAppenDataForArea(result.data.cityItem, binding!!.areaSpinner)
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

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }




    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()


    }


    fun updateCartForDeleivery(){
        viewModel.getDeliveryInfo().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.status.status == 1) {

                        Toast.makeText(context, result.data.status.msg, Toast.LENGTH_SHORT).show()

                        binding!!.firstName.setText("")
                        binding!!.lastName.setText("")
                        binding!!.phoneNumberEt.setText("")
binding!!.pd.hide()
                        onPhoneSubmitListener.invoke("1")
                        mListener?.OnCloseDialog("1")






                    } else {
                        Toast.makeText(context, result.data.status.msg, Toast.LENGTH_SHORT).show()
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }}
fun getcart(){
    viewModel.getCart().observe(viewLifecycleOwner) { result ->
        when (result) {
            is NetworkResults.Success -> {

                if (result.data.status.status == 1) {


//                    Toast.makeText(context,result.data.status.msg.toString() , Toast.LENGTH_SHORT).show()

                    viewModel.updateDeliveryCart(result.data.user_cart.order_id,areaId,
                        binding!!.firstName.text.toString(), binding!!.lastName.text.toString(),
                        binding!!.phoneNumberEt.text.toString())

                } else {
                    Toast.makeText(context, result.data.status.msg.toString(), Toast.LENGTH_SHORT).show()
                }

            }
            is NetworkResults.Error -> {
                result.exception.printStackTrace()
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }}




    override fun onStop() {
        super.onStop()
        mListener?.OnCloseDialog("1")
//        if (mListener != null) mListener!!.OnCloseDialog(null)
    }
}









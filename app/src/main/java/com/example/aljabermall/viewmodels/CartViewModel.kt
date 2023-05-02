package com.example.aljabermall.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.fragments.CartFragment
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.*
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)
    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    private val addToCartLiveData = MutableLiveData<NetworkResults<Message>>()
    private val updateCartLiveData = MutableLiveData<NetworkResults<Message>>()
    private val deleteFromCartLiveData = MutableLiveData<NetworkResults<Message>>()
    private val cartCheckoutLiveData = MutableLiveData<NetworkResults<Message>>()
    private val cartLiveData = MutableLiveData<NetworkResults<UserCartModel>>()
    private val promoCodeLiveData = MutableLiveData<NetworkResults<PromoCodeModel>>()
    private val addPhoneNumberLiveData = MutableLiveData<NetworkResults<MessageModel>>()
    private val addCityLiveData = MutableLiveData<NetworkResults<CityModel>>()

    private val addAreaLiveData = MutableLiveData<NetworkResults<CityModel>>()
    private val updateDeliveryCartLiveData = MutableLiveData<NetworkResults<Message>>()
    private val updateDeliveryCartForTakeAwayLiveData = MutableLiveData<NetworkResults<Message>>()


    private val removePromoCodeLiveData = MutableLiveData<NetworkResults<PromoCodeModel>>()

    private val orderDetailLiveData = MutableLiveData<NetworkResults<DetailsInvoicesModel>>()
    private val invoicesLiveData = MutableLiveData<NetworkResults<InvoicesModel>>()


    fun cityData (){
        viewModelScope.launch {
            addCityLiveData.value = NetworkRepository.getCity(
                language,

                )
        }
    }


    fun areaData (tid:String){
        viewModelScope.launch {
            addAreaLiveData.value = NetworkRepository.getArea(
                tid,
                language,

                )
        }
    }
//    private val invoicesLiveData = liveData {
//        val results = NetworkRepository.getInvoices(language, uid)
//        emit(results)
//    }

    fun retriveInvoiceInfo() {
        viewModelScope.launch {
            invoicesLiveData.value =
                NetworkRepository.getInvoices(language,uid)
        }

    }

    fun retrieveCart() {
        viewModelScope.launch {
            cartLiveData.value = NetworkRepository.getCart(
                language,
                uid,

            )
            Log.d("uidsssaa",uid)
        }
    }

    fun addToCart(price: Double, productId: String, quantity: String) {
        viewModelScope.launch {
            addToCartLiveData.value =
                NetworkRepository.addToCart(productId, uid, quantity, price.toString(), language)
        }

    }

    fun updateCart(productId: String, quantity: String,order_idBody:String) {
        viewModelScope.launch {
            updateCartLiveData.value =
                NetworkRepository.updateCart(language, uid, productId, quantity,order_idBody.toString())
        }
    }
    fun updateDeliveryCart(order_id: String, tid: String, fName:String, lName: String, phone:String) {
        viewModelScope.launch {
            updateDeliveryCartLiveData.value =
                NetworkRepository.updateuserInfo( uid, order_id, tid,fName,lName, phone, "en")


        }
    }
    fun updateTakeAwayCart(order_id: String, tid: String) {
        viewModelScope.launch {
            updateDeliveryCartForTakeAwayLiveData.value =
                NetworkRepository.updateuserInfoForTakeAway( uid, order_id, tid, language)


        }
    }
    fun getOrderDetails(order_id: String) {
        viewModelScope.launch {
            Log.d("orderidsssaa", order_id)

            orderDetailLiveData.value =
                NetworkRepository.orderDetalis( uid, order_id, language)


        }
    }



    fun deleteFromCart(productId: String) {
        viewModelScope.launch {
            deleteFromCartLiveData.value =
                NetworkRepository.deleteFromCart(language, uid, productId)


            Log.d("UID@",uid.toString())
        }
    }

    fun checkPromoCode(order_id: String,promoCode: String) {
        viewModelScope.launch {
            promoCodeLiveData.value = NetworkRepository.checkPromoCode(language,order_id, uid, promoCode)
        }
    }

    fun removecheckPromoCode(order_id: String,promoCode: String) {
        viewModelScope.launch {
            removePromoCodeLiveData.value = NetworkRepository.removecheckPromoCode(language,order_id, uid, promoCode)
        }
    }
    fun addPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            addPhoneNumberLiveData.value = NetworkRepository.addPhoneNumber(
                language,
                uid,
                phoneNumber,
            )
        }
    }


    fun cartCheckout(

        oederId: Int,

    ) {
        viewModelScope.launch {
            cartCheckoutLiveData.value = NetworkRepository.cartCheckout(
                language,
                uid,
                oederId.toString(),

            )
        }
    }

    fun getCart() = cartLiveData

    fun getInvoices() = invoicesLiveData

    fun getAddToCartMessage() = addToCartLiveData

    fun getUpdateCartMessage() = updateCartLiveData

    fun getDeleteFromCartMessage() = deleteFromCartLiveData

    fun getPromoCodeResponse() = promoCodeLiveData

    fun getAddPhoneMessage() = addPhoneNumberLiveData
    fun gitCityData() = addCityLiveData
    fun getAreaData() = addAreaLiveData
    fun getDeliveryInfo() = updateDeliveryCartLiveData

    fun getTakeAwayInfo() = updateDeliveryCartForTakeAwayLiveData

    fun getCartCheckoutMessage() = cartCheckoutLiveData

    fun getRemovePromoCode() = removePromoCodeLiveData

    fun getOrderDetails() = orderDetailLiveData
    fun removeFavourite(toString: String) {

    }


}
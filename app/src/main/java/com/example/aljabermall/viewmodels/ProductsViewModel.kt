package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.*
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch

class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)
    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    private val productsSearchLiveData = MutableLiveData<NetworkResults<ProductSearchModel>>()

    private val relatedProductsLiveData = MutableLiveData<NetworkResults<RelatedProductModel>>()

    private val categoriesLiveData = MutableLiveData<NetworkResults<CategoriesModel>>()

    private val productsCategoriesLiveData = MutableLiveData<NetworkResults<CategoryProductModel>>()

    private val productsOnSale = MutableLiveData<NetworkResults<ProductsModel>>()
    private val products_Delas = MutableLiveData<NetworkResults<ProductsModel>>()
    private val productsSubCategoriesLiveData = MutableLiveData<NetworkResults<CategoryProductModel>>()


    private val productsLiveData = liveData {
        val result = NetworkRepository.getProducts(language, uid, deviceId)
        emit(result)
    }

    fun retrieveOnSaleProducts() {
        viewModelScope.launch {
            val result = NetworkRepository.getProductsOnSale(language, uid, deviceId)
            productsOnSale.value = result
        }
    }

    fun retriveproductsDeals(type:String) {
        viewModelScope.launch {
            val result = NetworkRepository.getProductsDeals(language, uid, type)
            products_Delas.value = result
        }
    }



    fun retrieveProductCategories() {
        viewModelScope.launch {
            productsCategoriesLiveData.value =
                NetworkRepository.getProductsCategories(language, uid, deviceId)
        }
    }

    fun retrieveCategories() {
        viewModelScope.launch {
            val result = NetworkRepository.getCategories(language, uid)
            categoriesLiveData.value = result
        }
    }

    fun retrieveRelatedProducts(productId: String) {
        viewModelScope.launch {
            relatedProductsLiveData.value =
                NetworkRepository.getRelatedProducts( language, uid,productId)
        }
    }

    fun searchProducts(searchTerm: String) {
        viewModelScope.launch {
            productsSearchLiveData.value = NetworkRepository.productSearch(
                language,
                uid,
                searchTerm,
//                deviceId,
            )
        }
    }



    fun retrieveProductSubCategories(cid:String) {
        viewModelScope.launch {
            productsSubCategoriesLiveData.value =
                NetworkRepository.getProductsSubCategories(language, uid, deviceId,cid)
        }
    }


    fun getProducts() = productsLiveData

    fun getCategories() = categoriesLiveData

    fun getProductsOnSale() = productsOnSale

    fun getProductsCate() = productsCategoriesLiveData

    fun getRelatedProducts() = relatedProductsLiveData

    fun getSearchProducts() = productsSearchLiveData
    fun getProudectDeals() = products_Delas

    fun getSubCategory()=productsSubCategoriesLiveData

}

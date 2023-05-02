package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.FavouriteMessageModel
import com.example.aljabermall.models.FavouriteModel
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)

    private val addToFavouriteLiveData = MutableLiveData<NetworkResults<FavouriteMessageModel>>()
    private val removeFavouriteLiveData = MutableLiveData<NetworkResults<FavouriteMessageModel>>()

    private val favouriteLiveData = MutableLiveData<NetworkResults<FavouriteModel>>()

    fun retrieveFavourite() {
        viewModelScope.launch {
            val result = NetworkRepository.getFavourite(language, uid)
            favouriteLiveData.value = result
        }
    }

    fun addToFavourite(productId: String) {
        viewModelScope.launch {
            addToFavouriteLiveData.value =
                NetworkRepository.addToFavourite(language, uid, productId)
        }
    }

    fun removeFavourite(productId: String) {
        viewModelScope.launch {
            removeFavouriteLiveData.value =
                NetworkRepository.removeFavourite(language, uid, productId)
        }
    }

    fun getFavourite() = favouriteLiveData

    fun getAddToFavouriteMessage() = addToFavouriteLiveData

    fun getRemoveFavouriteMessage() = removeFavouriteLiveData
}
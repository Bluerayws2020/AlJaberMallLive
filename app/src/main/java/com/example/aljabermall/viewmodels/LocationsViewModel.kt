package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.MessageModel
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.UserLocationsModel
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch

class LocationsViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)

    private val addUserLocationLiveData = MutableLiveData<NetworkResults<MessageModel>>()
    private val updateUserLocationLiveData = MutableLiveData<NetworkResults<MessageModel>>()
    private val deleteUserLocationLiveData = MutableLiveData<NetworkResults<MessageModel>>()

    private val userLocationsLiveData = MutableLiveData<NetworkResults<UserLocationsModel>>()
    private val addressDetailsLiveData = MutableLiveData<NetworkResults<UserLocationsModel>>()

    fun retrieveUserLocations() {
        viewModelScope.launch {
            val result = NetworkRepository.getUserLocations(language, uid)
            userLocationsLiveData.value = result
        }
    }


//    getAddressDetails

//    fun getAddressDetails(
//        profile_id: String
//    ) {
//        viewModelScope.launch {
//            val result = NetworkRepository.getAddressDetails(profile_id)
//            addressDetailsLiveData.value = result
//        }
//    }


    fun addUserLocation(
        language: String,
        userId: String,
        locality: String?,
        address_line1: String?,
        given_name: String?,
        family_name: String?

    ) {
        viewModelScope.launch {
            addUserLocationLiveData.value = NetworkRepository.addUserLocation(
                language,
                uid,
                locality,
                address_line1,
                given_name,
                family_name,
//                area,
//                street,
//                buildingNo
//                apartmentNo
            )
        }
    }

    fun updateUserLocation(
        uid: String,
        profile_id: String,
        locality: String,
        address_line1: String,
        given_name: String,
        family_name: String,
        language: String

    ) {
        viewModelScope.launch {
            updateUserLocationLiveData.value = NetworkRepository.updateUserLocation(
                uid,
                profile_id,
                locality,
                address_line1,
                given_name,
                family_name,
                language
            )
        }
    }

    fun deleteUserLocation(locationId: String,flag:String) {
        viewModelScope.launch {
            deleteUserLocationLiveData.value =
                NetworkRepository.deleteUserLocation(uid,flag, locationId)
        }
    }

    fun getUserLocations() = userLocationsLiveData

    fun getAddLocationMessage() = addUserLocationLiveData

    fun getDeleteLocationMessage() = deleteUserLocationLiveData

    fun getUpdateLocationMessage() = updateUserLocationLiveData

    fun getAddressDetailsMessage() = addressDetailsLiveData
}
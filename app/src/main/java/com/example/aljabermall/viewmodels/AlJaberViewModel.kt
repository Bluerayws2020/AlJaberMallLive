package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.AboutUsData
import com.example.aljabermall.models.Message
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.UserCartModel
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch

class AlJaberViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)

    private val aboutUsLiveData = MutableLiveData<NetworkResults<AboutUsData>>()
    private val contactUsLivedata = MutableLiveData<NetworkResults<Message>>()



    fun retriveAboutus() {
        viewModelScope.launch {
            aboutUsLiveData.value = NetworkRepository.getAboutUs(
                language,


            )
        }
    }
    fun retriveContatcUS(name:String,
                     email: String,
                     subject:String,
                     message: String) {
        viewModelScope.launch {
            contactUsLivedata.value = NetworkRepository.sendConactUs(
                language,
                uid,
                name,
                email,
                subject,
                message

                )
        }
    }

    fun getContactUsLivedaat() = contactUsLivedata
    fun getLiveAboutUS() = aboutUsLiveData


}
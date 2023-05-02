package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.MessageModel
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)
    private val repo = NetworkRepository

    private val editProfileLiveData = MutableLiveData<NetworkResults<MessageModel>>()

    private val userInfoLiveData = liveData {
        val result = repo.getUserInfo(language, uid)
        emit(result)
    }

    fun editUserProfile(
        userId: String,
        password: String? = null,
        rePassword: String? = null,
        phone: String? = null,
        name: String? = null,
        birthDate: String? = null,
        profileImage: File? = null,
        gender: String? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            editProfileLiveData.value = repo.editUseProfile(
                language,
                userId,
                rePassword,
                password,
                phone,
                name,
                birthDate,
                profileImage,
                gender,
                email
            )
        }
    }

    fun getUserInfo() = userInfoLiveData

    fun getEditProfileMessage() = editProfileLiveData
}
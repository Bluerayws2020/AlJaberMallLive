package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.*
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)
    private val repo = NetworkRepository

    private val loginMessageLiveData = MutableLiveData<NetworkResults<LoginModel>>()
    private val signUpMessageLiveData = MutableLiveData<NetworkResults<RegisterModel>>()
    private val resetPasswordLiveData = MutableLiveData<NetworkResults<MessageModel>>()
    private val sendOtpliveData = MutableLiveData<NetworkResults<MessageModel>>()
    private val resendOtpLiveData = MutableLiveData<NetworkResults<MessageModel>>()
    private val resetPasseord = MutableLiveData<NetworkResults<MessageModel>>()
    private val otpForForgetPass = MutableLiveData<NetworkResults<MessageModelForget>>()

    fun userLogin(email: String, password: String, deviceId: String) {
        viewModelScope.launch {
            loginMessageLiveData.value = repo.userLogin(language, email, password, deviceId)
        }
    }

    fun userSignUp(
        name: String,
        email: String,
        password: String,
        phone: String,

    ) {
        viewModelScope.launch {
            signUpMessageLiveData.value = repo.userSignUp(
                language,
                name,
                email,
                password,
                phone,
            )
        }
    }

    fun userSignUpProvider(
        email: String?,
        name: String?,
        userImage: String,
        loginProvider: String,
    ) {
        viewModelScope.launch {
            signUpMessageLiveData.value = repo.userSignUpProvider(
                language,
                email,
                deviceId,
                name,
                userImage,
                loginProvider,
                HelperUtils.FACEBOOK_OR_GOOGLE_PROVIDER
            )
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            resetPasswordLiveData.value = repo.resetPassword(language, email)
        }
    }


    fun sendOtp(phon:String,type:String,otp:String){
        viewModelScope.launch {
            sendOtpliveData.value = repo.sendOtp(phon,type,otp,language)
        }
    }


    fun resendOtp(phon:String,type:String){
        viewModelScope.launch {
            resendOtpLiveData.value = repo.reSendOtp(phon,type,language)
        }
    }


    fun resetPass(uid:String,newpass:String,cpass:String){
        viewModelScope.launch {
            resetPasseord.value = repo.resetPassword(uid,cpass,newpass,language)
        }
    }
    fun otpForForget(phone:String){
        viewModelScope.launch {
            otpForForgetPass.value = repo.otpForForget(phone,"2",language)
        }
    }
    fun getOtpForForget() = otpForForgetPass

    fun getResetPassword() = resetPasseord


    fun getSendOtp() = sendOtpliveData

    fun getResndOtp() = resendOtpLiveData

    fun getOtpResponse() = sendOtpliveData

    fun getLoginResponse() = loginMessageLiveData

    fun getSignUpResponse() = signUpMessageLiveData

    fun getResetPassResponse() = resetPasswordLiveData
}
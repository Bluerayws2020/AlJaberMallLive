package com.example.aljabermall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.aljabermall.helpers.HelperUtils
import com.example.aljabermall.models.NetworkResults
import com.example.aljabermall.models.UpdateUserBranchModel
import com.example.aljabermall.repository.NetworkRepository
import kotlinx.coroutines.launch

class BranchViewModel(application: Application) : AndroidViewModel(application) {
    private val language = HelperUtils.getLang(application.applicationContext)
    private val uid = HelperUtils.getUID(application.applicationContext)
    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    private val updateBranchLiveData = MutableLiveData<NetworkResults<UpdateUserBranchModel>>()
    private val branchesLiveData = liveData {
        val result = NetworkRepository.getBranches(language)
        emit(result)
    }

    fun updateBranches(branchId: String) {
        viewModelScope.launch {
            updateBranchLiveData.value =
                NetworkRepository.updateUserBranches(language, uid, branchId, deviceId)
        }
    }

    fun getBranches() = branchesLiveData

    fun getUpdateBranchesResponse() = updateBranchLiveData
}
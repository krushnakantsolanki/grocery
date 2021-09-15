package com.company.dreamgroceries.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.company.dreamgroceries.data.*
import com.company.dreamgroceries.webservices.Resource

class ProfileViewModel(private val profileRepo: ProfileRepo) : ViewModel() {


    fun getProfile(): LiveData<Profile> {
        return profileRepo.getUser()

    }

    fun updateProfile(updateProfileReq: UpdateProfileReq): LiveData<Resource<SocialLoginResponse>> {
        return profileRepo.updateProfile(updateProfileReq)
    }

    fun getOtp(otpRequest: OtpRequest): LiveData<Resource<OtpResponse>> {
        return profileRepo.getOtp(otpRequest)
    }

    fun updateNotificationSetting(updateNotificationReq: UpdateNotificationReq) :LiveData<Resource<BaseResponse>> {
        return profileRepo.updateNotificationSettings(updateNotificationReq)
    }
}
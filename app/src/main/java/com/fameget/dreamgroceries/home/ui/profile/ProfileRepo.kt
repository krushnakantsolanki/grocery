package com.fameget.dreamgroceries.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.OtpRequest
import com.fameget.dreamgroceries.data.Profile
import com.fameget.dreamgroceries.data.UpdateNotificationReq
import com.fameget.dreamgroceries.data.UpdateProfileReq
import com.fameget.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class ProfileRepo : BaseDataSource() {

    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }


    fun getUser(): LiveData<Profile> {
        return MyApp.getInstance().toInfoDao().getUser()
    }

    fun updateProfile(updateProfileReq: UpdateProfileReq) =
        resultLiveDataNoDb { apiCallForUpdateProfile(updateProfileReq) }
            .distinctUntilChanged()


    suspend fun apiCallForUpdateProfile(updateProfileReq: UpdateProfileReq) =
        getResult { apiService.updateProfile(updateProfileReq, getToken()) }

    fun getOtp(otpRequest: OtpRequest) =
        resultLiveDataNoDb { apiCallForGetOtp(otpRequest) }
            .distinctUntilChanged()

    suspend fun apiCallForGetOtp(otpRequest: OtpRequest) =
        getResult { apiService.getOtp(otpRequest) }

    suspend fun updateNotificationSettingsCall(updateNotificationReq: UpdateNotificationReq) =
        getResult { apiService.updateNotificationSettingsCall(updateNotificationReq, getToken()) }

    fun updateNotificationSettings(updateNotificationReq: UpdateNotificationReq) =
        resultLiveDataNoDb { updateNotificationSettingsCall(updateNotificationReq) }
            .distinctUntilChanged()


}
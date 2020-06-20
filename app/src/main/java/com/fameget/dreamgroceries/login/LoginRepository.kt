package com.fameget.dreamgroceries.login

import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.*
import com.fameget.dreamgroceries.utilities.BEARER
import com.fameget.dreamgroceries.utilities.PreferenceHelper
import com.fameget.dreamgroceries.webservices.ApiService
import javax.inject.Inject


class LoginRepository :BaseDataSource() {

    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }

    suspend fun getOtp(otpRequest: OtpRequest) = apiService.getOtp(otpRequest)


    suspend fun socialMedialSignIn(socialLoginRequest: SocialLoginRequest) =
        apiService.socialMediaSignIn(socialLoginRequest)

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)
    suspend fun registerUser(registerUserRequest: UserRegRequest) = apiService.registerUser(
        registerUserRequest,
        BEARER + PreferenceHelper.getValue(
            PreferenceHelper.ACCESS_TOKEN,
            "",
            MyApp.getAppContext()
        ) as String
    )

    fun updateProfile(updateProfileReq: UpdateProfilePhone) =
        resultLiveDataNoDb { apiCallForUpdateProfile(updateProfileReq) }
            .distinctUntilChanged()


    suspend fun apiCallForUpdateProfile(updateProfileReq: UpdateProfilePhone) =
        getResult { apiService.updateProfile2(updateProfileReq, getToken()) }


}

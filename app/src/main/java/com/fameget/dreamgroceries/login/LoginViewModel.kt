package com.fameget.dreamgroceries.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.data.*
import com.fameget.dreamgroceries.utilities.PreferenceHelper
import com.fameget.dreamgroceries.utilities.PreferenceHelper.IS_LOGGED_IN
import com.fameget.dreamgroceries.utilities.PreferenceHelper.USER_ID
import com.fameget.dreamgroceries.utilities.Utils
import com.fameget.dreamgroceries.webservices.Resource
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val loginRepo: LoginRepository) : ViewModel() {


    public lateinit var mOtpRequest: OtpRequest

    fun getOtp(request: OtpRequest) = liveData(Dispatchers.IO) {
        mOtpRequest = request

        emit(Resource.loading(data = null))
        try {
            val response = Resource.success(
                data = loginRepo.getOtp(
                    mOtpRequest
                )
            )
            if (response.data!!.isSuccessful) {

                emit(Resource.success(response.data?.body()))
            } else {
                response.data.errorBody()?.let {
                    emit(
                        Resource.error(
                            data = null,
                            message = Utils.getErrorFromErrorBody(it)
                        )
                    )
                }

            }
        } catch (exception: Exception) {
            Log.e("tag", exception.localizedMessage.toString())
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    /*fun callAPI(type: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
    }*/


    fun socialMediaSignIn(socialLoginRequest: SocialLoginRequest) = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))

        try {
            val response = Resource.success(
                data = loginRepo.socialMedialSignIn(
                    socialLoginRequest
                )
            )
            if (response.data!!.isSuccessful) {
                val socialLoginResponse = response.data.body()

                socialLoginResponse?.let {
                    PreferenceHelper.setValue(IS_LOGGED_IN, true, MyApp.getAppContext())
                    PreferenceHelper.setValue(USER_ID, it.data.id, MyApp.getAppContext())
                    PreferenceHelper.setValue(
                        PreferenceHelper.ACCESS_TOKEN,
                        it.access_token,
                        MyApp.getAppContext()
                    )
                    PreferenceHelper.setValue(
                        PreferenceHelper.REFRESH_TOKEN,
                        it.refresh_token,
                        MyApp.getAppContext()
                    )
                    emit(Resource.success(response.data.body()))
                    MyApp.getInstance().toInfoDao().insertUser(it.data)
                    MyApp.getInstance().addressDao().insertAddress(it.data.addresses)
                }


            } else {
                response.data.errorBody()?.let {
                    emit(
                        Resource.error(
                            data = null,
                            message = Utils.getErrorFromErrorBody(it)
                        )
                    )
                }

            }
        } catch (exception: Exception) {
            Log.e("tag", exception.localizedMessage.toString())
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun login(otp: String, fcmToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))

        try {
            val response = Resource.success(
                data = loginRepo.login(
                    getLoginRequest(otp, fcmToken)

                )
            )
            if (response.data!!.isSuccessful) {
                val socialLoginResponse = response.data.body()

                socialLoginResponse?.let {
                    if (it.data.profile_complete == 1 )
                        PreferenceHelper.setValue(IS_LOGGED_IN, true, MyApp.getAppContext())
                    PreferenceHelper.setValue(USER_ID, it.data.id, MyApp.getAppContext())
                    PreferenceHelper.setValue(
                        PreferenceHelper.ACCESS_TOKEN,
                        it.access_token,
                        MyApp.getAppContext()
                    )
                    PreferenceHelper.setValue(
                        PreferenceHelper.REFRESH_TOKEN,
                        it.refresh_token,
                        MyApp.getAppContext()
                    )
                    emit(Resource.success(response.data.body()))
                    MyApp.getInstance().toInfoDao().insertUser(it.data)
                      MyApp.getInstance().addressDao().insertAddress(it.data.addresses)
                } ?: emit(Resource.error(data = null, message = "Something went wrong"))


            } else {

                response.data.errorBody()?.let {
                    emit(
                        Resource.error(
                            data = null,
                            message = Utils.getErrorFromErrorBody(it)
                        )
                    )
                } ?: emit(Resource.error(data = null, message = "Something went wrong"))


            }
        } catch (exception: Exception) {
            Log.e("tag", exception.localizedMessage.toString())
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    private fun getLoginRequest(otp: String, fcmToken: String): LoginRequest {
        return LoginRequest(
            mOtpRequest.country_code.toInt(),
            mOtpRequest.phone_number,
            otp.toInt(),
            fcmToken,
            1
        )

    }

    fun registerUser(userRegRequest: UserRegRequest) =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(data = null))

            try {

                val response = Resource.success(
                    data = loginRepo.registerUser(userRegRequest)
                )

                if (response.data!!.isSuccessful) {
                    val socialLoginResponse = response.data.body()
                    PreferenceHelper.setValue(IS_LOGGED_IN, true, MyApp.getAppContext())

                    socialLoginResponse?.let {
                        PreferenceHelper.setValue(USER_ID, it.data.id, MyApp.getAppContext())
                        emit(Resource.success(it))
                        MyApp.getInstance().toInfoDao().insertUser(it.data)
                         MyApp.getInstance().addressDao().insertAddress(it.data.addresses)
                    } ?: emit(Resource.error(data = null, message = "Something went wrong"))


                } else {
                    response.data.errorBody()?.let {
                        emit(
                            Resource.error(
                                data = null,
                                message = Utils.getErrorFromErrorBody(it),
                                errorCode = response.data.code()

                            )
                        )
                    } ?: emit(Resource.error(data = null, message = "Something went wrong"))

                }
            } catch (exception: Exception) {
                Log.e("tag", exception.localizedMessage.toString())
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }

    public fun getRegisterUserRequest(
        firstName: String,
        lastName: String,
        email: String,
        street: String,
        apt: String,
        zipcode: String,
        city: String,
        state: String,
        latitude: String,
        longitude: String
    ): UserRegRequest {
        return UserRegRequest(
            first_name = firstName,
            last_name = lastName,
            email = email,
            street = street,
            floor = apt,
            city =  city,
            state = state,
            zip_code = zipcode,
            latitude = latitude,
            longitude = longitude
        )
    }

    fun updateProfile(updateProfileReq: UpdateProfilePhone): LiveData<Resource<SocialLoginResponse>> {
        return loginRepo.updateProfile(updateProfileReq)
    }

}
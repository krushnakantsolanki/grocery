package com.company.dreamgroceries.base

import android.util.Log
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.utilities.BEARER
import com.company.dreamgroceries.utilities.PreferenceHelper
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Resource
import retrofit2.Response

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            response.errorBody()?.let {
                val message = Utils.getErrorFromErrorBody(it)
                if (message.isNotEmpty())
                    return error(message, response.code())

            }
            return error(" ${response.code()} ${response.message()}", response.code())
        } catch (e: Exception) {
            return error(e.message ?: e.toString(), 13)
        }
    }

    private fun <T> error(message: String, code: Int): Resource<T> {
        Log.e("error", message + " " + code)
        return Resource.error(
            message = message,
            data = null,
            errorCode = code
        )
    }

    protected fun getToken(): String {
        return "$BEARER${PreferenceHelper.getValue(
            PreferenceHelper.ACCESS_TOKEN,
            "",
            MyApp.getAppContext()
        ) as String}"
    }
}


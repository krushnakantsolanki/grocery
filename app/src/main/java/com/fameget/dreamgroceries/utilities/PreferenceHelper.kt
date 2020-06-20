package com.fameget.dreamgroceries.utilities

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    @JvmField
    var FCM_TOKEN: String = "fcm_token"
    const val REFRESH_TOKEN = "ref_token"
    const val ACCESS_TOKEN = "access_token"
    const val IS_LOGGED_IN = "is_user_logged_in"
    const val USER_ID = "user_id"
    const val FIRST_LAUNCH = "is_first_launch"

    fun getPref(context: Context): SharedPreferences {

        return context.applicationContext.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
    }

    fun setValue(key: String, value: Any, context: Context) =
        when (value) {
            is String -> getPref(context).edit().putString(key, value).commit()
            is Int -> getPref(context).edit().putInt(key, value).commit()
            is Long -> getPref(context).edit().putLong(key, value).commit()
            is Boolean -> getPref(context).edit().putBoolean(key, value).commit()
            else -> throw UnsupportedOperationException("Not supported")
        }


    fun getValue(key: String, defaultValue: Any, context: Context): Any? =
        when (defaultValue) {
            is String -> getPref(context).getString(key, defaultValue)
            is Int -> getPref(context).getInt(key, defaultValue)
            is Long -> getPref(context).getLong(key, defaultValue)
            is Boolean -> getPref(context).getBoolean(key, defaultValue)
            else ->
                throw UnsupportedOperationException("Un implemented")

        }

    fun clearAll(context: Context) {
        getPref(context).edit().clear().commit()
    }

}
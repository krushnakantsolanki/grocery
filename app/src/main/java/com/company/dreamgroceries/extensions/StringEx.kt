package com.company.dreamgroceries.extensions

import android.util.Log
import android.util.Patterns
import com.company.dreamgroceries.utilities.DATE_FORMAT
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/*
 Keep All extensions function with string here
*/


/**
 * validate following conditions
 *  - One capital letter
 *  - One number
 *  - One symbol (@,$,%,&,#,) whatever normal symbols that are acceptable.
 */
fun String.isValidPassword(): Boolean {
    return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        .matcher(this).matches()
}

/**
 * validate email
 * */
fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidMobile(): Boolean {
    return if (Pattern.matches("[0-9]+", this)) {
        this.length == 10
    } else false
}

fun String.toFormattedDate(): String {

    try {
        val cal: Calendar = Calendar.getInstance()
        val tz: TimeZone = cal.getTimeZone()


        Log.d("Time zone: ", tz.getDisplayName())


        val sdf = SimpleDateFormat(DATE_FORMAT)
        sdf.setTimeZone(tz)


        val timestamp: Long = this.toLong()
        val localTime: String =
            sdf.format(Date(timestamp * 1000)) // I assume your timestamp is in seconds and you're converting to milliseconds?

        return localTime
    } catch (e: Exception) {
        Log.e("error", e.localizedMessage)
    }
    return ""


    return ""
}

/*inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

inline fun <reified T> String.fromJson(): T = Gson().fromJson<T>(this, genericType<T>())*/
inline fun <reified T> String.fromJson(): T = Gson().fromJson(this, T::class.java)
inline fun <reified T> T.toJsonString(): String = Gson().toJson(this, T::class.java)


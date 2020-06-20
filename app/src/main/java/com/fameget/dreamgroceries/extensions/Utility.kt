package com.fameget.dreamgroceries.extensions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Convert any POJO to map
 */
fun <T> T.convertToMap(): Map<String, String> = with(Gson().toJson(this)) {
    Gson().fromJson<Map<String, String>>(
        this,
        object : TypeToken<Map<String, String>>() {}.type
    ) ?: emptyMap()
}

fun String.dmyStringToDate() = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this)
fun Date.dateTodmyString() = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(this)

inline fun <reified T> getType(): Type = object : TypeToken<T>() {}.type

/*
fun showProgress() {
    BeepApp.dialogInstance.showProgressDialog()
}

fun dismissProgress() {
    BeepApp.dialogInstance.dismissProgressDialog()
}*/

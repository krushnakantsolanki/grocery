package com.company.dreamgroceries.extensions
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast

fun Context.toastError(msg: String) {
    val toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    val toastView = toast.view // This'll return the default View of the Toast.
    toastView.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
    toastView.findViewById<TextView>(android.R.id.message)?.apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
        setTextColor(Color.WHITE)
        setPadding(5, 0, 5, 0)
    }
    toast.show()
}
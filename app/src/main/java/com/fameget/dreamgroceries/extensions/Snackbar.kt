package com.fameget.dreamgroceries.extensions
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun View.snack(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG) =
    snack(resources.getString(messageRes), length)

fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) =
    Snackbar.make(this, message, length)
        .apply {
            show()
        }

fun View.snackError(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG) =
    snack(resources.getString(messageRes), length)

fun View.snackError(message: String, length: Int = Snackbar.LENGTH_LONG) =
    Snackbar.make(this, message, length)
        .apply {
            setBackgroundTint(ContextCompat.getColor(this@snackError.context, android.R.color.holo_red_dark))
            setTextColor(ContextCompat.getColor(this@snackError.context,android.R.color.white))
            show()
        }

fun View.snack(@StringRes messageRes: Int, @StringRes actionRes: Int, listener: (View) -> Unit) =
    snack(resources.getString(messageRes), resources.getString(actionRes),listener)

fun View.snack(message: String,actionText:CharSequence,listener: (View) -> Unit) =
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
        .apply {
            setAction(actionText,listener)
            show()
        }
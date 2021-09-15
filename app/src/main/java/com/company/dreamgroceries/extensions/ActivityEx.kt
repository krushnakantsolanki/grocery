package com.company.dreamgroceries.extensions

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.ViewModelFactory

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideKeyboard() {
    this.apply {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}

fun Activity.exitAnimation(){
    overridePendingTransition(
        R.anim.slide_in_left,
        R.anim.slide_out_right
    )
}

fun Activity.enterAnimation(){
    overridePendingTransition(
        R.anim.slide_in_right,
        R.anim.slide_out_left
    )
}


inline fun <reified T : ViewModel> FragmentActivity.getViewModelFactory() =
    ViewModelProvider(this, ViewModelFactory()).get<T>()

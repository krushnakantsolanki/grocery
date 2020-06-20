package com.fameget.dreamgroceries.extensions
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideKeyboard() {
    this.activity?.hideKeyboard()
}

/*
inline fun <reified T : ViewModel> Fragment.getViewModelFactory() =
    ViewModelProvider(this, beepViewModelFactory()).get<T>()*/

package com.company.dreamgroceries.extensions
import androidx.fragment.app.Fragment

/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideKeyboard() {
    this.activity?.hideKeyboard()
}

/*
inline fun <reified T : ViewModel> Fragment.getViewModelFactory() =
    ViewModelProvider(this, beepViewModelFactory()).get<T>()*/

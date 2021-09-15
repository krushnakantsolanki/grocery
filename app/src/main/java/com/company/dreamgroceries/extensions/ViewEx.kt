package com.company.dreamgroceries.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.company.dreamgroceries.R
import com.google.android.material.textfield.TextInputLayout

/**
 * Extension method to provide hide keyboard for [View].
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * validate whether [TextInputLayout] is empty or not
 *
 * @return true if [TextInputLayout]] has empty text
 */
fun TextInputLayout.isTilEmpty(requestFocus: Boolean = true): Boolean {
    if (editText?.text.toString().isEmpty()) {
        error = editText?.context?.getString(R.string.can_not_empty)
        editText?.also {
            if (requestFocus)
                it.requestFocus()
            it.addTextChangedListener(
                onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                    if (!charSequence.isNullOrEmpty()) {
                        error = null
                    }
                }
            )
        }

        return true
    }
    return false
}


fun TextInputLayout.isTilNotValidCard(requestFocus: Boolean = true): Boolean {
    if (editText?.text.toString().length<16) {
        error = editText?.context?.getString(R.string.card_no_not_valid)
        editText?.also {
            if (requestFocus)
                it.requestFocus()
            it.addTextChangedListener(
                onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                    if (!charSequence.isNullOrEmpty()) {
                        error = null
                    }
                }
            )
        }

        return true
    }
    return false
}

fun TextInputLayout.isValidCVV(requestFocus: Boolean = true): Boolean {
    if (editText?.text.toString().length<3) {
        error = editText?.context?.getString(R.string.cvv_not_valid)
        editText?.also {
            if (requestFocus)
                it.requestFocus()
            it.addTextChangedListener(
                onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                    if (!charSequence.isNullOrEmpty()) {
                        error = null
                    }
                }
            )
        }

        return true
    }
    return false
}

fun TextInputLayout.isValidExp(requestFocus: Boolean = true): Boolean {
    if (editText?.text.toString().length<4) {
        error = editText?.context?.getString(R.string.card_expiry_not_valid)
        editText?.also {
            if (requestFocus)
                it.requestFocus()
            it.addTextChangedListener(
                onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                    if (!charSequence.isNullOrEmpty()) {
                        error = null
                    }
                }
            )
        }

        return true
    }
    return false
}

/**
 * set error to  [TextInputLayout]
 *
 * @param errorMsg
 */
fun TextInputLayout.setTilError(errorMsg: String) {
    error = errorMsg
    editText?.also {
        it.requestFocus()
        it.addTextChangedListener(
            onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                if (!charSequence.isNullOrEmpty()) {
                    error = null
                }
            }
        )
    }
}

/**
 * validate [TextInputLayout] has valid email
 *
 * @return true if valid email
 */
fun TextInputLayout.isTilValidEmail(): Boolean {
    return if (editText?.text.toString().isValidEmail()) {
        true
    } else {
        error = "Invalid email address."
        editText?.also {
            it.requestFocus()
            it.addTextChangedListener(
                onTextChanged = { charSequence: CharSequence?, _: Int, _: Int, _: Int ->
                    if (!charSequence.isNullOrEmpty()) {
                        error = null
                    }
                }
            )
        }
        false
    }
}


fun ImageView.loadCircleCrop(obj: Any) {
    Glide.with(this.context)
        .load(obj)
        .circleCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun ImageView.loadImage(obj: Any) {
    Glide.with(this.context)
        .load(obj)
        .placeholder(R.drawable.ic_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

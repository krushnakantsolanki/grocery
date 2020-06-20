package com.fameget.dreamgroceries.customviews

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.utilities.TypefaceCache
import com.google.android.material.textfield.TextInputEditText

/**
 *
 * EditText for setting custom typeface.
 */
class CFEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        var fontName: String? = null
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs,
                R.styleable.CFView
            )
            fontName = a.getString(R.styleable.CFView_fontName)
            a.recycle()
        }
        setFont(fontName)
    }

    fun setFont(fontName: String?) {
        if (isInEditMode) return
        typeface = TypefaceCache.fetchTypeface(
            context,
            if (!TextUtils.isEmpty(fontName)) fontName else context.getString(R.string.font_regular)
        )
    }
}
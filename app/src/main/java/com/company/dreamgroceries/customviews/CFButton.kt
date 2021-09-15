package com.company.dreamgroceries.customviews

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.company.dreamgroceries.R
import com.company.dreamgroceries.utilities.TypefaceCache

/**
 *
 * TextView for setting custom typeface.
 */
class CFButton : AppCompatButton {
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
        val a = context.obtainStyledAttributes(attrs,
            R.styleable.CFView
        )
        val fontName = a.getString(R.styleable.CFView_fontName)
        a.recycle()
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
package com.fameget.dreamgroceries.customviews

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.utilities.TypefaceCache

/**
 *
 * TextView for setting custom typeface.
 */
class CFTextView : AppCompatTextView {
    private var mOnLayoutListener: OnLayoutListener? = null

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

    interface OnLayoutListener {
        fun onLayouted(view: AppCompatTextView?)
    }

    fun setOnLayoutListener(listener: OnLayoutListener?) {
        mOnLayoutListener = listener
    }

    fun setFont(fontName: String?) {
        if (isInEditMode()) return
        setTypeface(
            TypefaceCache.fetchTypeface(
                getContext(),
                if (!TextUtils.isEmpty(fontName)) fontName else getContext().getString(
                    R.string.font_regular
                )
            )
        )
    }


}
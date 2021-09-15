package com.company.dreamgroceries.home.ui.orders

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.company.dreamgroceries.R
import com.company.dreamgroceries.databinding.ActivityProductDetailBinding


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = com.company.dreamgroceries.databinding.ActivityProductDetailBinding.inflate(
            layoutInflater
        )
        setContentView(mBinding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val calHeight: Int = (height * 0.6).toInt()
        val params = RelativeLayout.LayoutParams(
            1440, calHeight
        )
        params.addRule(RelativeLayout.ALIGN_START, R.id.llAnchor)
        params.addRule(RelativeLayout.ALIGN_END, R.id.llAnchor)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.llAnchor)
        mBinding.conMain.layoutParams = params

    }
}

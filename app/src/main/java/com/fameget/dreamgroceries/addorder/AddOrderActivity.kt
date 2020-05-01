package com.fameget.dreamgroceries.addorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.*
import com.fameget.dreamgroceries.databinding.AddOrderActivityBinding
import com.fameget.dreamgroceries.utilities.BUN_1

class AddOrderActivity : AppCompatActivity(), ManualOrderFragment.ManualFragmentListener,
    ViewCartFragment.ViewCartFragmentListener, ThanksFragment.ThanksFragmentListener {

    private var mType: Int? = 0
    private lateinit var mBinding: AddOrderActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AddOrderActivityBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mType = intent.extras?.getInt(BUN_1, 0)
    }

    override fun onBackPressed() {
        if (findNavController(R.id.nav_order).currentDestination?.id == R.id.thanksFragment) {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        } else if (findNavController(R.id.nav_order).currentDestination?.id == R.id.productsFragment) {
            if (getForegroundFragment() is ProductsFragment) {
                val productFragment: ProductsFragment = getForegroundFragment() as ProductsFragment
                productFragment.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    public fun getForegroundFragment(): Fragment? {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_order)

        return fragment?.childFragmentManager?.fragments?.get(0)


    }

    override fun getType(): Int? {
        return mType
    }
}

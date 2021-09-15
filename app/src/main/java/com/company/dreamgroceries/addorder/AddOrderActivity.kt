package com.company.dreamgroceries.addorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.company.dreamgroceries.*
import com.company.dreamgroceries.addorder.fragments.*
import com.company.dreamgroceries.base.BaseActivity
import com.company.dreamgroceries.databinding.AddOrderActivityBinding
import com.company.dreamgroceries.utilities.BUN_1

class AddOrderActivity : BaseActivity(), ManualOrderFragment.ManualFragmentListener,
    ViewCartFragment.ViewCartFragmentListener, ThanksFragment.ThanksFragmentListener,ProductsFragment.ProductsFragmentListener,MakePaymentFragment.MakePaymentFragmentListener {

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

        } /*else if (findNavController(R.id.nav_order).currentDestination?.id == R.id.productsFragment) {
            if (getForegroundFragment() is ProductsFragment) {
                val productFragment: ProductsFragment = getForegroundFragment() as ProductsFragment
                productFragment.onBackPressed()
            }
        }*/ else {
            super.onBackPressed()
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    public override fun getMyFragmentManager(): FragmentManager? {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_order)

        //return fragment?.childFragmentManager?.fragments?.get(0)
        return fragment?.childFragmentManager


    }

    override fun getType(): Int? {
        return mType
    }
}

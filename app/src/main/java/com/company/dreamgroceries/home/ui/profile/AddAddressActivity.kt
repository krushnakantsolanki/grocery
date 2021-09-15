package com.company.dreamgroceries.home.ui.profile

import android.os.Bundle
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseActivity
import com.company.dreamgroceries.databinding.ActivityAddAddressBinding
import com.company.dreamgroceries.utilities.BUN_1

class AddAddressActivity : BaseActivity() {

    private lateinit var fragment: AddAddressFragment
    private lateinit var mBinding: ActivityAddAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment =
            AddAddressFragment.newInstance(intent?.getParcelableExtra(BUN_1))
        fragmentTransaction.replace(R.id.container, fragment, "add_address")
        fragmentTransaction.commit()
    }


    override fun onBackPressed() {

        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )


    }
}

package com.fameget.dreamgroceries.home.ui.profile

import android.os.Bundle
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.base.BaseActivity
import com.fameget.dreamgroceries.databinding.ActivityAddAddressBinding
import com.fameget.dreamgroceries.utilities.BUN_1

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

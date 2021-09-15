package com.company.dreamgroceries.home.ui.profile

import android.os.Bundle
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseActivity
import com.company.dreamgroceries.data.UpdateProfileReq
import com.company.dreamgroceries.databinding.ActivityEnterOtpBinding
import com.company.dreamgroceries.login.fragments.EnterOtpFragment
import com.company.dreamgroceries.utilities.BUN_1

class EnterOtpActivity : BaseActivity() {

    private lateinit var mBinding: ActivityEnterOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEnterOtpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EnterOtpFragment.newInstance(intent.getParcelableExtra<UpdateProfileReq>(
                    BUN_1)))
                .commitNow()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )

    }
}

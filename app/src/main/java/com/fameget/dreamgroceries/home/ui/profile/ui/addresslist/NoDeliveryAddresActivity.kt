package com.fameget.dreamgroceries.home.ui.profile.ui.addresslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fameget.dreamgroceries.NoDeliveryAddressFragment
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.databinding.ActivityNoDeliveryAddresBinding
import com.fameget.dreamgroceries.utilities.BUN_1

class NoDeliveryAddresActivity : AppCompatActivity() {

    private lateinit var fragment: NoDeliveryAddressFragment
    private lateinit var mBinding: ActivityNoDeliveryAddresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNoDeliveryAddresBinding.inflate(layoutInflater)
        val title = intent.getStringExtra(BUN_1)
        setContentView(mBinding.root)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment = NoDeliveryAddressFragment().apply {
            arguments = Bundle().apply {
                putString(BUN_1, title)

            }
        }
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }
}

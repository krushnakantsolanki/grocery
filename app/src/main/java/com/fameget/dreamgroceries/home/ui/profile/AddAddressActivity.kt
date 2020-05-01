package com.fameget.dreamgroceries.home.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fameget.dreamgroceries.AddAddressFragment
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.databinding.ActivityAddAddressBinding

class AddAddressActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityAddAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = AddAddressFragment()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}

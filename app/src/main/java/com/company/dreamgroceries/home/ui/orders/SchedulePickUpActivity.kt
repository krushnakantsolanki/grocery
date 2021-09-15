package com.company.dreamgroceries.home.ui.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.fragments.SchedulePickupFragment
import com.company.dreamgroceries.databinding.ActivitySchedulePickUpBinding

class SchedulePickUpActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySchedulePickUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySchedulePickUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment =
            SchedulePickupFragment()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}

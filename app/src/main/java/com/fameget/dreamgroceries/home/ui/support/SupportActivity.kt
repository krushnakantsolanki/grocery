package com.fameget.dreamgroceries.home.ui.support

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.home.ui.profile.AddAddressFragment
import com.fameget.dreamgroceries.utilities.BUN_1

class SupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = SupportFragment()

        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
        findViewById<ImageView>(R.id.ivBack).setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {

        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )


    }
}

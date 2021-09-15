package com.company.dreamgroceries.home.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseActivity
import com.company.dreamgroceries.home.ui.profile.ui.addresslist.AddressListFragment
import com.company.dreamgroceries.utilities.BUN_1

class AddressListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_list_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddressListFragment.newInstance(intent.getIntExtra(BUN_1,0)))
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

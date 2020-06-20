package com.fameget.dreamgroceries.home.ui.orders

import android.os.Bundle
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.base.BaseActivity
import com.fameget.dreamgroceries.data.Order
import com.fameget.dreamgroceries.utilities.BUN_1
import com.fameget.dreamgroceries.utilities.BUN_2

class ViewOrderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = ViewOrderFragment()
        val bundle = Bundle()
        if (intent.getParcelableExtra<Order>(BUN_1) != null)
            bundle.putParcelable(BUN_1, intent.getParcelableExtra(BUN_1))
        if (intent.getStringExtra(BUN_2) != null)
            bundle.putString(BUN_2, intent.getStringExtra(BUN_2))
        fragment.arguments = bundle


        fragmentTransaction.replace(R.id.flContainer, fragment)
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

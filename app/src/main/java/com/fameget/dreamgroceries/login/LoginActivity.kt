package com.fameget.dreamgroceries.login

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.base.BaseActivity


class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        AsyncTask.execute { MyApp.getInstance().clearAllTables() }

    }

    override fun onBackPressed() {
        /* if (findNavController(R.id.nav_host).currentDestination?.id == R.id.enterDetailsFragment) {
             val fragment =
                 getMyFragmentManager()?.fragments?.get(0) as EnterDetailsFragment
             if (!fragment.hideNoDataViewIfVisible()) {
                 super.onBackPressed()
             }

         } else {*/
        super.onBackPressed()
        //}

    }

    public fun getMyFragmentManager(): FragmentManager? {
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host)

        //return fragment?.childFragmentManager?.fragments?.get(0)
        return fragment?.childFragmentManager


    }


}

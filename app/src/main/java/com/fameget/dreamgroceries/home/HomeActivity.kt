package com.fameget.dreamgroceries.home

import android.app.Activity
import android.content.*
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.customviews.CFTextView
import com.fameget.dreamgroceries.data.NotCountResponse
import com.fameget.dreamgroceries.databinding.ActivityHomeBinding
import com.fameget.dreamgroceries.extensions.getViewModelFactory
import com.fameget.dreamgroceries.home.ui.home.HomeFragment
import com.fameget.dreamgroceries.home.ui.orders.MyOrdersFragment
import com.fameget.dreamgroceries.home.ui.orders.ViewOrderActivity
import com.fameget.dreamgroceries.home.ui.profile.EditProfileActivity
import com.fameget.dreamgroceries.home.ui.profile.ProfileFragment
import com.fameget.dreamgroceries.home.ui.support.SupportFragment
import com.fameget.dreamgroceries.login.LoginActivity
import com.fameget.dreamgroceries.notification.NotificationActivity
import com.fameget.dreamgroceries.utilities.*
import com.fameget.dreamgroceries.webservices.Resource
import com.fameget.dreamgroceries.webservices.Status
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.layout_side_menu.*


class HomeActivity : AppCompatActivity() {

    private lateinit var mReceiver: BroadcastReceiver
    private var mNotificationCount: Int? = 0
    private lateinit var tvNoCount: CFTextView
    private var mSelectedId: Int = 0
    private var mCurrentFragment: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    val viewModel by lazy {
        getViewModelFactory<HomeViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val orderNo = intent.getStringExtra(BUN_1)
        Log.e("Home", "Home " + intent.getStringExtra(BUN_1))
        if (orderNo != null) {
            startViewOrderActivity(orderNo)
        }
        tvNoCount = findViewById<CFTextView>(R.id.tvNotCount)
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mReceiver = MyReceiver()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        supportActionBar?.setTitle("")


        if (orderNo != null) {
            updateNotificationCount()
            markLayoutSelected(rlOrders)
            markLayoutNotSelected(rlHome)
            markLayoutNotSelected(rlProfile)
            markLayoutNotSelected(rlSupport)
            openFragment(R.id.rlOrders, false)

        } else {
            markLayoutSelected(rlHome)
            markLayoutNotSelected(rlOrders)
            markLayoutNotSelected(rlProfile)
            markLayoutNotSelected(rlSupport)
            openFragment(R.id.rlHome, false)
        }


// Set the drawer toggle as the DrawerListener
// Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(view: View, v: Float) {}
            override fun onDrawerOpened(view: View) {}
            override fun onDrawerClosed(view: View) {
                if (mSelectedId != mCurrentFragment) {
                    when (mSelectedId) {
                        R.id.rlOrders -> showOrderFragment()
                        R.id.rlHome -> openHomeFragment(true)
                        R.id.rlSupport -> showSupportFragment()
                        R.id.rlProfile -> showProfileFragment()
                        R.id.rlLogout -> showLogoutDialog()
                        else -> {
                        }
                    }
                }


            }

            override fun onDrawerStateChanged(i: Int) {}
        })



        rlOrders.setOnClickListener {
            mSelectedId = R.id.rlOrders
            drawerLayout.close()

        }

        rlHome.setOnClickListener {
            mSelectedId = R.id.rlHome
            drawerLayout.close()

        }


        rlProfile.setOnClickListener {
            mSelectedId = R.id.rlProfile
            drawerLayout.close()
        }

        rlSupport.setOnClickListener {
            mSelectedId = R.id.rlSupport
            drawerLayout.close()
        }
        rlLogout.setOnClickListener {
            mSelectedId = R.id.rlLogout
            drawerLayout.close()

        }
        findViewById<ImageView>(R.id.ivDrawer).setOnClickListener {
            markCurrentFragmentSelectedInDrawer()
            drawerLayout.open()
        }

        ivNotification.setOnClickListener {
            if (mCurrentFragment == R.id.rlProfile) {

                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }


    }

    private fun markCurrentFragmentSelectedInDrawer() {
        when (mCurrentFragment) {
            R.id.rlHome -> {
                markLayoutSelected(rlHome)
                markLayoutNotSelected(rlOrders)
                markLayoutNotSelected(rlProfile)
                markLayoutNotSelected(rlSupport)
            }
            R.id.rlOrders -> {
                markLayoutSelected(rlOrders)
                markLayoutNotSelected(rlHome)
                markLayoutNotSelected(rlProfile)
                markLayoutNotSelected(rlSupport)
            }
            R.id.rlProfile -> {
                markLayoutSelected(rlProfile)
                markLayoutNotSelected(rlOrders)
                markLayoutNotSelected(rlHome)
                markLayoutNotSelected(rlSupport)
            }
            R.id.rlProfile -> {
                markLayoutSelected(rlSupport)
                markLayoutNotSelected(rlOrders)
                markLayoutNotSelected(rlHome)
                markLayoutNotSelected(rlProfile)
            }
        }
    }

    private fun startViewOrderActivity(orderNo: String) {
        val intent = Intent(this, ViewOrderActivity::class.java)
        intent.putExtra(BUN_2, orderNo)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mReceiver,
            IntentFilter(ACTION_UPDATE_NOT_COUNT)
        )
        updateNotificationCount()
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(mReceiver)
        } catch (e: Exception) {

        }
    }


    private fun updateNotificationCount() {
        viewModel.getNotificationCount().observe(this, Observer { handleResponse(it) })
    }

    private fun handleResponse(resource: Resource<NotCountResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {

                mNotificationCount = resource.data?.unread_notifications
                if (mNotificationCount == 0) {
                    tvNoCount.visibility = View.GONE
                } else {
                    if (mCurrentFragment == R.id.rlHome || mCurrentFragment == R.id.rlSupport) {
                        tvNoCount.visibility = View.VISIBLE
                        tvNoCount.text = mNotificationCount.toString()
                    }
                }

            }
            Status.ERROR -> {


                Utils.showToast(this, resource.message)
            }
            Status.LOADING -> {

            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val orderNo = intent?.getStringExtra(BUN_1)
        Log.e("Home", "Home " + intent?.getStringExtra(BUN_1))
        if (orderNo != null) {
            val intent = Intent(this, ViewOrderActivity::class.java)
            intent.putExtra(BUN_2, orderNo)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            markLayoutSelected(rlOrders)
            openFragment(R.id.rlOrders, false)
        } else {
            markLayoutSelected(rlHome)
            openFragment(R.id.rlHome, false)
        }
    }

    private fun showSupportFragment() {
        showNotCount()
        openFragment(R.id.rlSupport, true)
        markLayoutSelected(rlSupport)
        markLayoutNotSelected(rlHome)
        markLayoutNotSelected(rlProfile)
        markLayoutNotSelected(rlOrders)
    }

    private fun showProfileFragment() {
        tvNoCount.visibility = View.GONE
        openFragment(R.id.rlProfile, true)
        markLayoutSelected(rlProfile)
        markLayoutNotSelected(rlOrders)
        markLayoutNotSelected(rlHome)
        markLayoutNotSelected(rlSupport)
    }

    /*private fun showHomeFrgment() {
        openFragment(R.id.rlHome, true)
        markLayoutSelected(rlHome)
        markLayoutNotSelected(rlOrders)
        markLayoutNotSelected(rlProfile)
        markLayoutNotSelected(rlSupport)
    }*/

    fun openHomeFragment(isAnimate: Boolean) {
        showNotCount()
        markLayoutSelected(rlHome)
        markLayoutNotSelected(rlOrders)
        markLayoutNotSelected(rlProfile)
        markLayoutNotSelected(rlSupport)
        openFragment(R.id.rlHome, isAnimate)
    }

    private fun showOrderFragment() {
        openFragment(R.id.rlOrders, true)
        markLayoutSelected(rlOrders)
        markLayoutNotSelected(rlHome)
        markLayoutNotSelected(rlProfile)
        markLayoutNotSelected(rlSupport)
    }

    private fun showLogoutDialog() {

        Utils.showDialog(
            this,
            getString(R.string.logout),
            getString(R.string.are_you_sure_logout),
            getString(R.string.yes),
            getString(R.string.no),
            DialogInterface.OnClickListener { dialogInterface, i ->
                moveToLoginActivity()
            },
            null
        )


    }

    private fun moveToLoginActivity() {
        PreferenceHelper.clearAll(this@HomeActivity)
        AsyncTask.execute {
            MyApp.getInstance().toInfoDao().delete()
            MyApp.getInstance().addressDao().delete()
            MyApp.getInstance().cartDao().deleteAll()
        }

        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()

    }


    private fun markLayoutSelected(view: View) {
        view.isSelected = true
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun markLayoutNotSelected(view: View) {
        view.isSelected = false
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }


    private fun openFragment(id: Int, isAnimate: Boolean) {
        mCurrentFragment = id
        drawerLayout.close()

        val fragment = when (id) {
            R.id.rlOrders -> MyOrdersFragment()
            R.id.rlProfile -> ProfileFragment()
            R.id.rlSupport -> SupportFragment()

            else -> HomeFragment()
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (isAnimate)
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.replace(R.id.frameContainer, fragment)
        transaction.commit()
        if (id == R.id.rlProfile) {
            ivNotification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))
        } else {
            ivNotification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_noti))
        }


        if (id == R.id.rlOrders) {
            tvNoCount.visibility = View.GONE
            ivNotification.visibility = View.GONE
        } else {
            ivNotification.visibility = View.VISIBLE
        }
    }

    private fun showNotCount() {
        mNotificationCount?.let {
            if (it > 0)
                tvNoCount.visibility = View.VISIBLE
            else
                tvNoCount.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_ORDER && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(BUN_1, R.id.rlSupport)
            if (id == R.id.rlSupport) {
                openFragment(R.id.rlSupport, false)
                markLayoutSelected(rlSupport)
                markLayoutNotSelected(rlHome)
                markLayoutNotSelected(rlProfile)
                markLayoutNotSelected(rlOrders)
            } else if (id == R.id.rlOrders) {
                openFragment(R.id.rlOrders, false)
                markLayoutSelected(rlOrders)
                markLayoutNotSelected(rlHome)
                markLayoutNotSelected(rlProfile)
                markLayoutNotSelected(rlSupport)
                startViewOrderActivity(data.getStringExtra(BUN_2))
            }
        }
    }

    inner class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updateNotificationCount()

        }
    }


}

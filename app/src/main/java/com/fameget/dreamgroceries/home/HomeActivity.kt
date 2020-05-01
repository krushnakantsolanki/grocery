package com.fameget.dreamgroceries.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.SupportFragment
import com.fameget.dreamgroceries.databinding.ActivityHomeBinding
import com.fameget.dreamgroceries.home.ui.home.HomeFragment
import com.fameget.dreamgroceries.home.ui.orders.MyOrdersFragment
import com.fameget.dreamgroceries.home.ui.profile.EditProfileActivity
import com.fameget.dreamgroceries.home.ui.profile.ProfileFragment
import com.fameget.dreamgroceries.login.LoginActivity
import com.fameget.dreamgroceries.utilities.REQUEST_ADD_ORDER
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.layout_side_menu.*


class HomeActivity : AppCompatActivity() {

    private var mCurrentFragment: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /*appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )*/
        /* setupActionBarWithNavController(navController, appBarConfiguration)
         navView.setupWithNavController(navController)*/

        /*toolbar.post {
            val d =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_drawer, null)
            toolbar.setNavigationIcon(d)
        }*/

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        supportActionBar?.setTitle("")

        markLayoutSelected(rlHome)
        openFragment(R.id.rlHome)

        // NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        // NavigationUI.setupWithNavController(navView, navController);

        //navigationView.setNavigationItemSelectedListener(this);

        rlOrders.setOnClickListener {
            openFragment(R.id.rlOrders)
            markLayoutSelected(rlOrders)
            markLayoutNotSelected(rlHome)
            markLayoutNotSelected(rlProfile)
            markLayoutNotSelected(rlSupport)
        }

        rlHome.setOnClickListener {
            openFragment(R.id.rlHome)
            markLayoutSelected(rlHome)
            markLayoutNotSelected(rlOrders)
            markLayoutNotSelected(rlProfile)
            markLayoutNotSelected(rlSupport)
        }


        rlProfile.setOnClickListener {
            openFragment(R.id.rlProfile)
            markLayoutSelected(rlProfile)
            markLayoutNotSelected(rlOrders)
            markLayoutNotSelected(rlHome)
            markLayoutNotSelected(rlSupport)
        }

        rlSupport.setOnClickListener {
            openFragment(R.id.rlSupport)
            markLayoutSelected(rlSupport)
            markLayoutNotSelected(rlHome)
            markLayoutNotSelected(rlProfile)
            markLayoutNotSelected(rlOrders)
        }
        rlLogout.setOnClickListener {
            drawerLayout.close()
            showLogoutDialog()
        }
        findViewById<ImageView>(R.id.ivDrawer).setOnClickListener { drawerLayout.open() }

        ivNotification.setOnClickListener {
            if (mCurrentFragment == R.id.rlProfile) {

                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout))
        builder.setMessage(getString(R.string.are_you_sure_logout))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }
        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton: Button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton: Button = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        // Change the alert dialog buttons text and background color

        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(ContextCompat.getColor(this,R.color.textColor))
        positiveButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white))

        negativeButton.setTextColor(ContextCompat.getColor(this,R.color.textColor))
        negativeButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white))


    }


    private fun markLayoutSelected(view: View) {
        view.isSelected = true
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun markLayoutNotSelected(view: View) {
        view.isSelected = false
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }


/*   override fun onSupportNavigateUp(): Boolean {

       )
   }*/


    private fun openFragment(id: Int) {
        mCurrentFragment = id
        /*val d =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_drawer, null)
        toolbar.setNavigationIcon(d)*/
        drawerLayout.close()

        val fragment = when (id) {
            R.id.rlOrders -> MyOrdersFragment()
            R.id.rlProfile -> ProfileFragment()
            R.id.rlSupport -> SupportFragment()

            else -> HomeFragment()
        }
        // navController.navigate(id);
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commit()
        if (id == R.id.rlProfile) {
            ivNotification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))
        } else {
            ivNotification.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_noti))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_ORDER && resultCode == Activity.RESULT_OK) {
            openFragment(R.id.rlOrders)
            markLayoutSelected(rlOrders)
            markLayoutNotSelected(rlHome)
            markLayoutNotSelected(rlProfile)
            markLayoutNotSelected(rlSupport)
        }
    }


}

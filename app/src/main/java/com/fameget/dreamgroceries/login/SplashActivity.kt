package com.fameget.dreamgroceries.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.databinding.ActivitySpashBinding
import com.fameget.dreamgroceries.home.HomeActivity
import com.fameget.dreamgroceries.utilities.BUN_1
import com.fameget.dreamgroceries.utilities.PreferenceHelper

class SplashActivity : AppCompatActivity() {

    private lateinit var mRunnable: Runnable
    private lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySpashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mHandler = Handler()


    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(getRunnable(), 1500)
    }

    private fun getRunnable(): Runnable {
        mRunnable = Runnable {
            val isLoggedIn: Boolean = PreferenceHelper.getValue(
                PreferenceHelper.IS_LOGGED_IN,
                false,
                this@SplashActivity
            ) as Boolean
            if (isLoggedIn) {
                val intent = Intent(SplashActivity@ this, HomeActivity::class.java)
                if (intent.extras != null) {
                    for (key in intent.extras!!.keySet()) {
                        if (key.equals("order_no")) {
                            val value = intent.extras!!.getString(key)
                            Log.d("splash", "Key: $key Value: $value")
                            intent.putExtra(BUN_1, value)

                        }
                    }
                } else if (getIntent().getStringExtra("order_no") != null)
                    intent.putExtra(BUN_1, getIntent().getStringExtra("order_no"))

                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


            } else {
                startActivity(
                    Intent(
                        SplashActivity@ this,
                        LoginActivity::class.java
                    )

                )
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            this@SplashActivity.finish()
        }
        return mRunnable
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

}

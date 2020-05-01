package com.fameget.dreamgroceries.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fameget.dreamgroceries.databinding.ActivitySpashBinding

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
            startActivity(
                Intent(
                    SplashActivity@ this,
                    LoginActivity::class.java
                )

            )
            this@SplashActivity.finish()
        }
        return mRunnable
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

}

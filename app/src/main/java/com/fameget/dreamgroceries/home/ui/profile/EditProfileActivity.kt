package com.fameget.dreamgroceries.home.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnNext.setOnClickListener { onBackPressed()  }

        mBinding.ivBack.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

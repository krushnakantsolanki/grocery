package com.fameget.dreamgroceries.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.di.AppComponent

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    public fun getAppComponent(): AppComponent {

        return MyApp.getAppComponent()
    }

    protected fun showProgress(progressBar2: ProgressBar?) {
        progressBar2?.visibility = View.VISIBLE
        // if (!requireActivity().isFinishing) {
        //CustomProgressDialog.showProgressDialog(requireActivity() as BaseActivity)
        //}

    }

    protected fun hideProgress(progressBar2: ProgressBar?) {
        progressBar2?.visibility = View.GONE
        //CustomProgressDialog.dismissProgressDialog()
    }
}
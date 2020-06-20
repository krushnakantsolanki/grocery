package com.fameget.dreamgroceries.customviews

import android.app.Dialog
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.base.BaseActivity


object CustomProgressDialog {

    private var mDialog: Dialog? = null

    /**Show Progress Dialog*/
    fun showProgressDialog(activity: BaseActivity) {

        mDialog?.takeUnless { it.isShowing }?.show() ?: createDialog(activity).show()
    }

    /**
     * Create new dialog instance for every activity
     *
     * @return
     */
    private fun createDialog(activity: BaseActivity): Dialog {
        mDialog = Dialog(activity, R.style.ProgressDialog).apply {
            setContentView(R.layout.dialog_progress)
            setCancelable(false)
        }
        return mDialog as Dialog
    }

    /**Dismiss Progress Dialog*/
    fun dismissProgressDialog() {
        mDialog?.takeIf { it.isShowing }?.dismiss()
    }

}

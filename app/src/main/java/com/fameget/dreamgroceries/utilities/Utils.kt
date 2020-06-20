package com.fameget.dreamgroceries.utilities

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.customviews.CFTextView
import com.fameget.dreamgroceries.data.BaseResponse
import com.fameget.dreamgroceries.data.Product
import com.fameget.dreamgroceries.data.SocialLoginResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import java.text.ParseException
import java.text.SimpleDateFormat

object Utils {


    fun showToast(context: Context, string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    fun showToastShort(context: Context, string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

   /* fun isAnyValidAddressForDelivery(data: SocialLoginResponse?): Boolean {
        val list = data?.data?.addresses
        list?.let {
            for (address in it) {
                if (address.allow_order == 1) {
                    return true
                }
            }
        }
        return false

    }*/

    fun isNetworkConnected(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }


        return result
    }

    fun getErrorMessage(code: Int?, message: String): String {
        return message
    }

    fun getErrorFromErrorBody(it: ResponseBody): String {
        try {
            val gson = Gson()
            val type = object : TypeToken<BaseResponse>() {}.type
            var errorResponse: BaseResponse =
                gson.fromJson(it.charStream(), type)
            return errorResponse.message
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage)
        }
        return "";


    }

    fun showDialog(
        context: Context,
        title: String,
        desc: String,
        okText: String,
        cancelText: String,
        okListener: DialogInterface.OnClickListener,
        cancelListener: DialogInterface.OnClickListener?,
        neutralText: String? = null,
        neutralClickListener: DialogInterface.OnClickListener? = null
    ) {
        AlertDialog.Builder(context)
            .setMessage(desc)
            .setTitle(title)
            .setNegativeButton(cancelText, cancelListener)
            .setPositiveButton(okText, okListener)
            .setNeutralButton(neutralText, neutralClickListener)
            .create()
            .show()
    }

    fun getTotalPrice(it: List<Product>?): Double {
        var finalPrice: Double = 0.0
        it?.let {
            for (product in it) {
                product.mrp?.let {
                    finalPrice += (product.mrp.toDouble() * product.cart_count)
                }

            }
        }
        return "%.2f".format(finalPrice).toDouble()

    }

    fun getTotalTax(it: List<Product>?): Double {
        var finalPrice: Double = 0.0
        it?.let { it ->
            for (product in it) {
                product.total_tax?.let {
                    finalPrice += (product.total_tax.toDouble() * product.cart_count)
                }
                    ?: run {
                        val factor: Double? = product.tax_percentage?.toDouble()?.times(0.01)
                        if (product.mrp != null && factor != null) {
                            val taxPerProduct: Double = product.mrp.toDouble() * factor
                            finalPrice += taxPerProduct * product.cart_count
                        }
                    }

            }
        }
        return "%.2f".format(finalPrice).toDouble()
    }

    fun getTimeInSeconds(date: String): Long {

        //String date_ = date;
        val sdf = SimpleDateFormat(DATE_FORMAT);
        try {
            val mDate = sdf.parse(date);
            return mDate.getTime() / 1000;
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;


    }

    fun setOrderStatusAndColor(tvOrderStatus: CFTextView, status: Int) {
        when (status) {
            PENDING ->
                setTextAndBgColor(
                    R.color.pending,
                    tvOrderStatus.context.getString(R.string.status_pending),
                    tvOrderStatus
                )
            CONFIRM ->
                setTextAndBgColor(
                    R.color.confirm,
                    tvOrderStatus.context.getString(R.string.status_confirm),
                    tvOrderStatus
                )

            IN_PROCESS ->
                setTextAndBgColor(
                    R.color.in_processing,
                    tvOrderStatus.context.getString(R.string.status_in_process),
                    tvOrderStatus
                )

            READY ->
                setTextAndBgColor(
                    R.color._ready_pick_up,
                    tvOrderStatus.context.getString(R.string.status_ready),
                    tvOrderStatus
                )

            OUT_FOR_DELIVERY ->
                setTextAndBgColor(
                    R.color.outfordelivery,
                    tvOrderStatus.context.getString(R.string.status_out_for_del),
                    tvOrderStatus
                )

            PICKED_UP ->
                setTextAndBgColor(
                    R.color.picked_up,
                    tvOrderStatus.context.getString(R.string.status_picked_up),
                    tvOrderStatus
                )

            DELIVERED ->
                setTextAndBgColor(
                    R.color.delivered,
                    tvOrderStatus.context.getString(R.string.status_delivered),
                    tvOrderStatus
                )

            ORDER_CANCELLED ->
                setTextAndBgColor(
                    R.color.cancelled,
                    tvOrderStatus.context.getString(R.string.status_order_cancelled),
                    tvOrderStatus
                )


        }
    }

    fun setTextAndBgColor(
        status: Int,
        string: String,
        tvOrderStatus: CFTextView
    ) {
        tvOrderStatus.text = string

        (tvOrderStatus.background as GradientDrawable).setColor(
            ContextCompat.getColor(tvOrderStatus.context, status)
        )
    }

}
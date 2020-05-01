package com.fameget.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R
import kotlinx.android.synthetic.main.item_my_orders.view.*

class MyOrdersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_orders, parent, false)
        return MyOrderHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myOrderStatus: MyOrderHolder = holder as MyOrderHolder;
        if (position == 1) {
            myOrderStatus.tvOrderStatus.setText("Order Received")
            myOrderStatus.tvOrderStatus.setBackgroundResource(R.drawable.bg_order_receive)
        } else if (position == 2) {
            myOrderStatus.tvOrderStatus.setText("Order Ready")
            myOrderStatus.tvOrderStatus.setBackgroundResource(R.drawable.bg_order_ready)
        }
    }


    inner class MyOrderHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val tvOrderStatus: TextView = mView.tvOrderStatus

    }
}
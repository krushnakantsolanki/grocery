package com.fameget.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R

class ViewCartProductsAdapter()


    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_product, parent, false)
        return CartProdcutHolder(view)
    }


    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }


    inner class CartProdcutHolder(val mView: View) : RecyclerView.ViewHolder(mView) {


    }
}
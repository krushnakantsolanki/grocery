package com.fameget.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.ProductsFragment
import com.fameget.dreamgroceries.R

class ProductsAdapter(
    var productClickListener: ProductClickListener

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductHolder(view)
    }


    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { productClickListener.onClick() }
    }


    inner class ProductHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }
}
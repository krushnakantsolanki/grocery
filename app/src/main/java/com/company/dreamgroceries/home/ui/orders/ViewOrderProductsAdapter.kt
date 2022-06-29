package com.company.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.dreamgroceries.R
import com.company.dreamgroceries.data.OrderProduct
import com.company.dreamgroceries.extensions.loadImage
import com.company.dreamgroceries.utilities.CURR
import kotlinx.android.synthetic.main.item_order_product.view.*
import kotlinx.android.synthetic.main.item_product.view.ivProduct
import kotlinx.android.synthetic.main.item_product.view.tvPrice
import kotlinx.android.synthetic.main.item_view_product.view.tvProductDesc

class ViewOrderProductsAdapter :
    ListAdapter<OrderProduct, RecyclerView.ViewHolder>(DiffCallbackOrder()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_product, parent, false)
        return CartProdcutHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var product = getItem(position)
        var productHolder: CartProdcutHolder = holder as CartProdcutHolder

        product.image?.let { productHolder.mView.ivProduct.loadImage(it) }
        productHolder.mView.tvProductDesc.setText(product.name)
        productHolder.mView.tvPrice.setText("$CURR${product.price}")
        productHolder.mView.tvQun.setText(productHolder.mView.tvQun.context.getString(R.string.qty,product.quantity.toString()))

    }

    class CartProdcutHolder(val mView: View) : RecyclerView.ViewHolder(mView) {


    }

}


private class DiffCallbackOrder : DiffUtil.ItemCallback<OrderProduct>() {

    override fun areItemsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
        return oldItem.order_id == newItem.order_id
    }

    override fun areContentsTheSame(oldItem: OrderProduct, newItem: OrderProduct): Boolean {
        return oldItem == newItem
    }
}
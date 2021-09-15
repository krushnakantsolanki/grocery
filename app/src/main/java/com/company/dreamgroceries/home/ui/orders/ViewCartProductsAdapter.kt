package com.company.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.dreamgroceries.R
import com.company.dreamgroceries.data.Product
import com.company.dreamgroceries.extensions.loadImage
import com.company.dreamgroceries.utilities.CURR
import kotlinx.android.synthetic.main.item_product.view.ivProduct
import kotlinx.android.synthetic.main.item_product.view.tvPrice
import kotlinx.android.synthetic.main.item_view_product.view.*

class ViewCartProductsAdapter(val listener: ViewCarttemClickListener) :
    ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallbackProduct()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_product, parent, false)
        return CartProdcutHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var product = getItem(position)
        var productHolder: CartProdcutHolder = holder as CartProdcutHolder

        product.image?.let { productHolder.mView.ivProduct.loadImage(it) }
        productHolder.mView.tvProductDesc.setText(product.name)
        productHolder.mView.tvPrice.setText("$CURR${product.mrp}")
        productHolder.mView.tvCount.setText(product.cart_count.toString())
        productHolder.mView.tvRemove.setOnClickListener { listener.removeProduct(product) }

        productHolder.mView.ivAddPrd.setOnClickListener {
            val count = productHolder.mView.tvCount.text.toString().toInt()
            productHolder.mView.tvCount.setText((count + 1).toString())
            product.cart_count = productHolder.mView.tvCount.text.toString().toInt()
            listener.updateProduct(product)
        }

        productHolder.mView.ivRemPrd.setOnClickListener {
            val count = productHolder.mView.tvCount.text.toString().toInt()
            if (count > 1)
                productHolder.mView.tvCount.setText((count - 1).toString())

            product.cart_count = productHolder.mView.tvCount.text.toString().toInt()
            listener.updateProduct(product)
        }


    }

    class CartProdcutHolder(val mView: View) : RecyclerView.ViewHolder(mView) {


    }

}


private class DiffCallbackProduct : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
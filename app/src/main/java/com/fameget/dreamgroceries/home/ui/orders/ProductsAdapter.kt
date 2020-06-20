package com.fameget.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.data.Product
import com.fameget.dreamgroceries.extensions.loadImage
import com.fameget.dreamgroceries.utilities.CURR
import com.fameget.dreamgroceries.webservices.NetStatus
import com.fameget.dreamgroceries.webservices.NetworkState
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class ProductsAdapter(val productClickListener: ProductClickListener) :
    PagedListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback()) {


    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_product -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product, parent, false)
                return ProductHolder(view)
            }
            R.layout.network_state_item -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_state_item, parent, false)
                return NetworkStateItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == R.layout.item_product) {
            var product = getItem(position)
            var productHolder: ProductHolder = holder as ProductHolder

            product?.image?.let { productHolder.mView.ivProduct.loadImage(it) }
            productHolder.mView.tvDesc.text = product?.name
            productHolder.mView.tvPrice.text = "$CURR${product?.mrp}"
            product?.run {
                productHolder.mView.setOnClickListener { productClickListener.onClick(this) }
            }
        } else {
            var networkHolder: NetworkStateItemViewHolder = holder as NetworkStateItemViewHolder
            networkHolder.mView.progress_bar.visibility =
                toVisibility(networkState?.status == NetStatus.RUNNING)
            networkHolder.mView.retry_button.visibility =
                toVisibility(networkState?.status == NetStatus.FAILED)
            networkHolder.mView.error_msg.visibility = toVisibility(networkState?.msg != null)
            networkHolder.mView.error_msg.text = networkState?.msg
            networkHolder.mView.retry_button.setOnClickListener {
                productClickListener.retry()
            }
        }

    }

    fun toVisibility(constraint: Boolean): Int {
        return if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.item_product
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }


    inner class ProductHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

    inner class NetworkStateItemViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

}

private class DiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
package com.company.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.company.dreamgroceries.R
import com.company.dreamgroceries.data.Order
import com.company.dreamgroceries.extensions.toFormattedDate
import com.company.dreamgroceries.utilities.*
import com.company.dreamgroceries.webservices.NetStatus
import com.company.dreamgroceries.webservices.NetworkState
import kotlinx.android.synthetic.main.item_my_orders.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*


class MyOrdersAdapter(val orderClickListener: OrderClickListener) :
    PagedListAdapter<Order, RecyclerView.ViewHolder>(DiffOrdersCallback()) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_my_orders -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_my_orders, parent, false)
                return ItemViewHolder(view)
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

        if (viewType == R.layout.item_my_orders) {
            var order = getItem(position)

            var orderHolder: ItemViewHolder = holder as ItemViewHolder

            order?.let {
                orderHolder.mView.tvOrderId.text = "#${order.order_no}"

                if (it.order_type == ORDER_MANUAL_DELIVERY || it.order_type == ORDER_MANUAL_PICKUP) {
                    if (order?.order_manual_products.isNotEmpty())
                        orderHolder.mView.tvPriceQun.setText(
                            orderHolder.mView.context.getString(
                                R.string.products_count,
                                order?.order_manual_products.size
                            )
                        )
                } else if (it.order_type == ORDER_BROWSE_PICKUP || it.order_type == ORDER_BROWSE_DELIVERY) {
                    orderHolder.mView.tvPriceQun.text = "$CURR${it.order_details?.total_amt}"
                }

                Utils.setOrderStatusAndColor(orderHolder.mView.tvOrderStatus, order?.status)
                orderHolder.mView.tvTime.text =
                    order?.created_at?.toFormattedDate()?.replace("am", "AM")?.replace("pm", "PM")

                orderHolder.mView.setOnClickListener { orderClickListener.onOrderClicked(order) }

                if (order?.status == PENDING || order.status == CONFIRM || order?.status == IN_PROCESS || order.status == READY) {
                    orderHolder.mView.tvCancelOrder.visibility = View.VISIBLE
                    orderHolder.mView.tvCancelOrder.setOnClickListener {
                        orderClickListener.onCancelOrder(
                            order, position
                        )
                    }
                } else
                    orderHolder.mView.tvCancelOrder.visibility = View.GONE


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
                orderClickListener.retry()
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


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.item_my_orders
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

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


    inner class ItemViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

    inner class NetworkStateItemViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }


}

private class DiffOrdersCallback : DiffUtil.ItemCallback<Order>() {

    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}
package com.fameget.dreamgroceries.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.customviews.CFTextView
import com.fameget.dreamgroceries.data.NotificationData
import com.fameget.dreamgroceries.extensions.toFormattedDate
import com.fameget.dreamgroceries.utilities.*
import com.fameget.dreamgroceries.webservices.NetStatus
import com.fameget.dreamgroceries.webservices.NetworkState
import kotlinx.android.synthetic.main.item_notification.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class NotificationAdapter(val notificationClickListener: NotificationClickListener) :
    PagedListAdapter<NotificationData, RecyclerView.ViewHolder>(NotDiffCallback()) {


    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_notification -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification, parent, false)
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
        if (viewType == R.layout.item_notification) {
            var notificationData = getItem(position) as NotificationData
            var notificationHolder: ItemViewHolder = holder as ItemViewHolder
            notificationHolder.mView.tvTitle.text = notificationData.message
            notificationHolder.mView.tvTime.text = notificationData.created.toFormattedDate()
            setIcon(notificationHolder.mView.ivImage, notificationData.type)
            notificationHolder.mView.setOnClickListener { notificationClickListener.onClick(notificationData) }
        } else {
            var networkHolder: NetworkStateItemViewHolder = holder as NetworkStateItemViewHolder
            networkHolder.mView.progress_bar.visibility =
                toVisibility(networkState?.status == NetStatus.RUNNING)
            networkHolder.mView.retry_button.visibility =
                toVisibility(networkState?.status == NetStatus.FAILED)
            networkHolder.mView.error_msg.visibility = toVisibility(networkState?.msg != null)
            networkHolder.mView.error_msg.text = networkState?.msg
             networkHolder.mView.retry_button.setOnClickListener {
                 notificationClickListener.retry()
             }
        }

    }

    private fun setIcon(ivImage: ImageView?, status: Int) {

            when (status) {
                /*PENDING ->
                    ivImage?.setImageDrawable(ContextCompat.getDrawable(ivImage.context,R.drawable.ic_confirm_order))*/
                CONFIRM ->
                    ivImage?.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivImage.context,
                            R.drawable.ic_confirm_order
                        )
                    )

                IN_PROCESS ->
                    ivImage?.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivImage.context,
                            R.drawable.ic_not_order_in_process
                        )
                    )

                READY ->
                    ivImage?.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivImage.context,
                            R.drawable.ic_not_ready
                        )
                    )
                OUT_FOR_DELIVERY ->
                    ivImage?.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivImage.context,
                            R.drawable.ic_not_out_for_del
                        )
                    )

                /*PICKED_UP ->
                    Utils.setTextAndBgColor(
                        R.color.picked_up,
                        tvOrderStatus.context.getString(R.string.status_picked_up),
                        tvOrderStatus
                    )
*/
                DELIVERED ->
                    ivImage?.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivImage.context,
                            R.drawable.ic_not_delivered
                        )
                    )

                ORDER_CANCELLED ->
                    ivImage?.setImageDrawable(
                        ContextCompat.getDrawable(
                            ivImage.context,
                            R.drawable.ic_not_cancel_order
                        )
                    )


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
            R.layout.item_notification
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


    inner class ItemViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

    inner class NetworkStateItemViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

}

private class NotDiffCallback : DiffUtil.ItemCallback<
        NotificationData>() {

    override fun areItemsTheSame(
        oldItem:
        NotificationData, newItem:
        NotificationData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem:
        NotificationData, newItem:
        NotificationData
    ): Boolean {
        return oldItem == newItem
    }
}
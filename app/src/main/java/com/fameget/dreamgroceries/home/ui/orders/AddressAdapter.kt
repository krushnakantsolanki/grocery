package com.fameget.dreamgroceries.home.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R
import kotlinx.android.synthetic.main.item_address.view.*

class AddressAdapter()


    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return AddressHolder(view)
    }


    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder: AddressHolder = holder as AddressHolder
        if (position == 1)
            holder.mView.ivSelect.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.mView.context,
                    R.drawable.ic_check
                )
            )
        else
            holder.mView.ivSelect.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.mView.context,
                    R.drawable.ic_un_check
                )
            )
    }


    inner class AddressHolder(val mView: View) : RecyclerView.ViewHolder(mView) {


    }
}
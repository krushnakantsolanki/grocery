package com.fameget.dreamgroceries.home.ui.profile

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.data.Addresse
import com.fameget.dreamgroceries.utilities.ADDRESS_DELETE
import com.fameget.dreamgroceries.utilities.ADDRESS_EDIT
import com.fameget.dreamgroceries.utilities.COMMA_SEP
import kotlinx.android.synthetic.main.item_address.view.*

class AddressListAdapter(private val listener: AddressClickListener?) :
    ListAdapter<Addresse, RecyclerView.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return AddressHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var address = getItem(position)
        var addressHolder: AddressHolder = holder as AddressHolder
        val list = ArrayList<String>()

        if (!TextUtils.isEmpty(address.floor.toString().trim())) {
            address.floor?.let { list.add(it) }
        }
        address.street?.let { list.add(it) }
        address.city?.let { list.add(it) }
        address.state?.let { list.add(it) }
        address.zip_code?.let { list.add(it) }
        addressHolder.mView.tvAddress1.text = TextUtils.join(COMMA_SEP, list)
        addressHolder.mView.tvEdit.setOnClickListener {
            listener?.onAddressClicked(address, ADDRESS_EDIT)
        }
        Log.e("isselected", "add " + address.is_selected)
        markAddressSelectUnSelect(holder, address)
        addressHolder.mView.setOnClickListener {
            if (address.is_selected == 0) {
                /*   if (address.is_selected == 1) {
                       address.is_selected = 0
                   } else {*/
                address.is_selected = 1
                //}
                listener?.onAddressSelected(address)
            }

        }



        addressHolder.mView.tvDelete.setOnClickListener {
            listener?.onAddressClicked(address, ADDRESS_DELETE)
        }

        if (listener == null) {
            addressHolder.mView.tvDelete.visibility = View.GONE
            addressHolder.mView.tvEdit.visibility = View.GONE
        }
    }

    private fun markAddressSelectUnSelect(
        holder: AddressHolder,
        address: Addresse
    ) {
        holder.mView.conContainer.isSelected = address.is_selected == 1
    }


    inner class AddressHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    }

}

private class DiffCallback : DiffUtil.ItemCallback<Addresse>() {

    override fun areItemsTheSame(oldItem: Addresse, newItem: Addresse): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Addresse, newItem: Addresse): Boolean {
        return false
    }
}
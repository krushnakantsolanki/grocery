package com.fameget.dreamgroceries.home.ui.profile

import com.fameget.dreamgroceries.data.Addresse

interface AddressClickListener {

    fun onAddressClicked(address: Addresse, type: Int)
    fun onAddressSelected(address: Addresse)

}
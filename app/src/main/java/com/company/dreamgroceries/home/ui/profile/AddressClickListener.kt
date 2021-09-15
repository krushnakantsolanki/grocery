package com.company.dreamgroceries.home.ui.profile

import com.company.dreamgroceries.data.Addresse

interface AddressClickListener {

    fun onAddressClicked(address: Addresse, type: Int)
    fun onAddressSelected(address: Addresse)

}
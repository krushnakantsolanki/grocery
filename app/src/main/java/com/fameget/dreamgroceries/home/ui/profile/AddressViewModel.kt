package com.fameget.dreamgroceries.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fameget.dreamgroceries.data.*
import com.fameget.dreamgroceries.webservices.Resource

class AddressViewModel(private val addressRepo: AddressRepo) : ViewModel() {


    fun getAddresses(): LiveData<List<Addresse>> {
        return addressRepo.getAddresses()

    }

    fun updateAddress(mAddress: Addresse): LiveData<Resource<AddressResponse>> {
        return addressRepo.updateAddressObserver(addresse = mAddress)
    }

    fun deleteAddress(address: Addresse): LiveData<Resource<AddressResponse>> {
        return addressRepo.deleteAddress(DeleteAddressReq(address.id))
    }

    fun createManualDelOrderReq(
        id: Int,
        productList: Array<String>,
        addInst: String
    ): ManualDelOrderReq {
        return ManualDelOrderReq(
            address_id = id,
            products = productList,
            additional_instructions = addInst
        )
    }

    fun placeManualDelOrder(createManualDelOrderReq: ManualDelOrderReq) : LiveData<Resource<PlaceOrderResponse>>{
        return addressRepo.manualDelOrderReq(createManualDelOrderReq)
    }


}
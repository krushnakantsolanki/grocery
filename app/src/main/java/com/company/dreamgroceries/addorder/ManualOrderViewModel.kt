package com.company.dreamgroceries.addorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.dreamgroceries.data.ManualPickUpOrderReq
import com.company.dreamgroceries.data.PlaceOrderResponse
import com.company.dreamgroceries.webservices.Resource
import java.util.*

class ManualOrderViewModel(val manualOrderRepo: ManualOrderRepo) : ViewModel() {
     var mTempList: ArrayList<String> = ArrayList()
    var pickTime: String? = null
    var addInst: String? = null
    var productList: MutableLiveData<List<String>> = MutableLiveData()

    fun createPickUpOrderReq(
        productList: Array<String>,
        addInst: String,
        timeInSeconds: String
    ): ManualPickUpOrderReq {
        return ManualPickUpOrderReq(
            products = productList,
            additional_instructions = addInst,
            pickup_date = timeInSeconds
        )
    }

    fun placePickUpOrder(createPickUpOrderReq: ManualPickUpOrderReq): LiveData<Resource<PlaceOrderResponse>> {
        return manualOrderRepo.manualPickUpOrderReq(createPickUpOrderReq)
    }


    fun getAddressCount(): LiveData<Int> {
        return manualOrderRepo.getAddressCount()
    }

    fun setValueToProductList(list: ArrayList<String>) {
       // productList.value = list
        mTempList =list
    }
}

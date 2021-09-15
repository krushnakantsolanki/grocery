package com.company.dreamgroceries.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.base.BaseDataSource
import com.company.dreamgroceries.base.resultLiveDataNoDb
import com.company.dreamgroceries.data.Addresse
import com.company.dreamgroceries.data.DeleteAddressReq
import com.company.dreamgroceries.data.ManualDelOrderReq
import com.company.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class AddressRepo : BaseDataSource() {


    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }

    fun getAddresses(): LiveData<List<Addresse>> {
        return MyApp.getInstance().addressDao().getAddresses()
    }

    suspend fun updateAddress(addresse: Addresse) =
        getResult { apiService.updateAddress(addresse, getToken()) }

    suspend fun deleteAddressCall(addresse: DeleteAddressReq) =
        getResult { apiService.deleteAddress(addresse, getToken()) }


    fun updateAddressObserver(addresse: Addresse) = resultLiveDataNoDb { updateAddress(addresse) }
        .distinctUntilChanged()

    fun deleteAddress(deleteAddressReq: DeleteAddressReq) =
        resultLiveDataNoDb { deleteAddressCall(deleteAddressReq) }
            .distinctUntilChanged()

    fun manualDelOrderReq(manualDelOrderReq: ManualDelOrderReq) =
        resultLiveDataNoDb { apiCallForDelReq(manualDelOrderReq) }
            .distinctUntilChanged()


    suspend fun apiCallForDelReq(manualDelOrderReq: ManualDelOrderReq) =
        getResult { apiService.placeDelOrderManual(manualDelOrderReq, getToken()) }


}
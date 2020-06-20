package com.fameget.dreamgroceries.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.Addresse
import com.fameget.dreamgroceries.data.DeleteAddressReq
import com.fameget.dreamgroceries.data.ManualDelOrderReq
import com.fameget.dreamgroceries.webservices.ApiService
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
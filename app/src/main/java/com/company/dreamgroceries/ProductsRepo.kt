package com.company.dreamgroceries

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.company.dreamgroceries.base.BaseDataSource
import com.company.dreamgroceries.base.resultLiveDataNoDb
import com.company.dreamgroceries.data.*
import com.company.dreamgroceries.utilities.BEARER
import com.company.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class ProductsRepo : BaseDataSource() {


    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }

    suspend fun getCategories(token: String) =
        getResult { apiService.getCategories("$BEARER$token") }

    suspend fun addProductsToCartCall(cartProducts: AddToCartReq) =
        getResult { apiService.addProductsToCartCall(getToken(), cartProducts) }

    suspend fun getProducts(productsRequest: ProductsRequest) =
        getResult { apiService.getProducts(getToken(), productsRequest) }


    fun getCategoriesObserver(token: String) = resultLiveDataNoDb { getCategories(token) }
        .distinctUntilChanged()

    fun getProductObserver(productsRequest: ProductsRequest) =
        resultLiveDataNoDb { getProducts(productsRequest) }.distinctUntilChanged()

    fun getCartProducts(): LiveData<List<Product>> {
        return MyApp.getInstance().cartDao().getCartProducts()
    }

    fun getCurrentAddress(): LiveData<Addresse> {
        return MyApp.getInstance().addressDao().geCurrentAddress()
    }

    fun getAddressCount(): LiveData<Int> {
        return MyApp.getInstance().addressDao().getAddressCount()
    }

    fun addProductsToCart(addProductsToCartReq: AddToCartReq) =
        resultLiveDataNoDb { addProductsToCartCall(addProductsToCartReq) }
            .distinctUntilChanged()

    suspend fun placeBrowseDeliveryCODOrderCall(browseDeliveryCODReq: BrowseDelOrderReq) =
        getResult { apiService.placeBrowseDeliveryCODOrderCall(getToken(), browseDeliveryCODReq) }


    fun placeBrowseDeliveryCODOrderReq(browseDeliveryCODReq: BrowseDelOrderReq) =
        resultLiveDataNoDb { placeBrowseDeliveryCODOrderCall(browseDeliveryCODReq) }
            .distinctUntilChanged()

    suspend fun placeBrowseDeliveryPickUpOrderCall(browsePickUpOrderReq: BrowsePickUpOrderReq) =
        getResult { apiService.placeBrowseDeliveryPickUpOrderCall(getToken(), browsePickUpOrderReq) }



    fun placeBrowseDeliveryPickUpOrderReq(browsePickUpOrderReq: BrowsePickUpOrderReq) =
        resultLiveDataNoDb { placeBrowseDeliveryPickUpOrderCall(browsePickUpOrderReq) }
            .distinctUntilChanged()

    fun getProductCartCount(id: Int): LiveData<Int> {
        return MyApp.getInstance().cartDao().getProductCartCount(id)
    }


}
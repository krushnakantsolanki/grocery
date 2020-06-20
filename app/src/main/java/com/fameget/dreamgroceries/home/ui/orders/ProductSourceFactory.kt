package com.fameget.dreamgroceries.home.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fameget.dreamgroceries.data.Product
import com.fameget.dreamgroceries.data.ProductsRequest


class ProductSourceFactory(val productsRequest: ProductsRequest) :
    DataSource.Factory<Int, Product>() {

    val productPageSource = MutableLiveData<ProductPageSource>()

    override fun create(): ProductPageSource {
        val productsRequest = ProductPageSource(productsRequest)
        productPageSource.postValue(productsRequest)
        return productsRequest
    }


}
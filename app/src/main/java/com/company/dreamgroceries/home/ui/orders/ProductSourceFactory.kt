package com.company.dreamgroceries.home.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.company.dreamgroceries.data.Product
import com.company.dreamgroceries.data.ProductsRequest


class ProductSourceFactory(val productsRequest: ProductsRequest) :
    DataSource.Factory<Int, Product>() {

    val productPageSource = MutableLiveData<ProductPageSource>()

    override fun create(): ProductPageSource {
        val productsRequest = ProductPageSource(productsRequest)
        productPageSource.postValue(productsRequest)
        return productsRequest
    }


}
package com.company.dreamgroceries.addorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.ProductsRepo
import com.company.dreamgroceries.data.*
import com.company.dreamgroceries.home.ui.orders.ProductPageSource
import com.company.dreamgroceries.home.ui.orders.ProductSourceFactory
import com.company.dreamgroceries.utilities.PreferenceHelper
import com.company.dreamgroceries.webservices.NetworkState
import com.company.dreamgroceries.webservices.Resource
import com.google.android.libraries.places.api.model.Place


class ProductsViewModel(private val productsRepo: ProductsRepo) : ViewModel() {


     var pickUpTime: String = ""
    var initialLoad: LiveData<NetworkState>
    var networkState: LiveData<NetworkState>
    private var itemDataSourceFactory: ProductSourceFactory
    var selectedAddressId: Int = 0
    lateinit var productsList: MutableList<Product>
    val productsReq = ProductsRequest(category_id = IntArray(0))
    val categories = productsRepo.getCategoriesObserver(
        PreferenceHelper.getValue(

            PreferenceHelper.ACCESS_TOKEN,
            "",
            MyApp.getAppContext()
        ) as String
    )

    fun getProducts(): LiveData<com.company.dreamgroceries.webservices.Resource<ProductsResponse>> {
        return productsRepo.getProductObserver(productsReq)
    }


    fun getCartProducts(): LiveData<List<Product>> {
        return productsRepo.getCartProducts()
    }

    fun getCurrentAddress(): LiveData<Addresse> {
        return productsRepo.getCurrentAddress()
    }

    fun getAddressCount(): LiveData<Int> {
        return productsRepo.getAddressCount()
    }

    fun addProductsToCart(currentList: List<Product>): LiveData<Resource<BaseResponse>> {
        return productsRepo.addProductsToCart(getAddProductsToCartReq(currentList))
    }

    private fun getAddProductsToCartReq(currentList: List<Product>): AddToCartReq {
        val list = ArrayList<CartItem>()
        for (product in currentList) {
            list.add(CartItem(product_id = product.id, quantity = product.cart_count))
        }
        return AddToCartReq(list)
    }

    fun placeBrowseDeliveryCODOrder(browseDeliveryCODReq: BrowseDelOrderReq): LiveData<Resource<PlaceOrderResponse>> {
        return productsRepo.placeBrowseDeliveryCODOrderReq(browseDeliveryCODReq)
    }


    fun placeBrowseDeliveryPickUpOrder(browseDeliveryPickUpReq: BrowsePickUpOrderReq): LiveData<Resource<PlaceOrderResponse>> {
        return productsRepo.placeBrowseDeliveryPickUpOrderReq(browseDeliveryPickUpReq)
    }

    fun getProductCount(id: Int): LiveData<Int> {
        return productsRepo.getProductCartCount(id)
    }


    var userPagedList: LiveData<PagedList<Product>>
    private var liveDataSource: LiveData<ProductPageSource>

    fun refresh() {

        itemDataSourceFactory.productPageSource.value?.invalidate()
    }


    init {
        itemDataSourceFactory = ProductSourceFactory(productsReq)
        networkState = Transformations.switchMap(
            itemDataSourceFactory.productPageSource
        ) { dataSource -> dataSource.networkState }

        initialLoad = Transformations.switchMap(
            itemDataSourceFactory.productPageSource
        ) { dataSource -> dataSource.initialLoad }

        liveDataSource = itemDataSourceFactory.productPageSource
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .build()
        userPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .build()
    }

    fun retry() {
        val listing = liveDataSource.value
        listing?.retry?.invoke()

    }

    fun resetProductsReq() {
        productsReq.page = 1
        productsReq.search = ""
        productsReq.category_id = IntArray(0)
    }
}



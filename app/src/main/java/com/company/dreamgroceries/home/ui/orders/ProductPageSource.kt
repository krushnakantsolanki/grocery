package com.company.dreamgroceries.home.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.data.Product
import com.company.dreamgroceries.data.ProductsRequest
import com.company.dreamgroceries.data.ProductsResponse
import com.company.dreamgroceries.utilities.BEARER
import com.company.dreamgroceries.utilities.PreferenceHelper
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.ApiService
import com.company.dreamgroceries.webservices.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


public class ProductPageSource(val productsRequest: ProductsRequest) :
    PageKeyedDataSource<Int, Product>() {

    @Inject
    lateinit var apiService: ApiService

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    var retry: (() -> Any)? = null

    init {
        MyApp.getAppComponent().inject(this)
    }

    //the size of a page that we want
    val PAGE_SIZE = 50

    //we will start from the first page which is 1
    private val FIRST_PAGE = 1


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        productsRequest.page = 1
        val call =
            apiService.getProductsNormal(productsRequest = productsRequest, token = getToken())
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.productsList
                    networkState.postValue(NetworkState.LOADED)
                    if (responseItems.isNullOrEmpty())
                        initialLoad.postValue(NetworkState.EMPTY)
                    else
                        initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(responseItems, null, productsRequest.page + 1)

                } else {

                    retry = {
                        loadInitial(params, callback)
                    }
                    networkState.postValue(
                        NetworkState.error(getErrorMessage(response))
                    )
                    initialLoad.postValue(
                        NetworkState.error(getErrorMessage(response))
                    )
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                val error = NetworkState.error(t.message ?: "unknown error")
                retry = {
                    loadInitial(params, callback)
                }
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        })


    }

    private fun getErrorMessage(response: Response<ProductsResponse>): String? {
        response.errorBody()?.let {
            val message = Utils.getErrorFromErrorBody(it)
            if (message.isNotEmpty())
                return message

        }
        return " ${response.code()} ${response.message()}"
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        networkState.postValue(NetworkState.LOADING)
        productsRequest.page = params.key.toInt()
        val call =
            apiService.getProductsNormal(productsRequest = productsRequest, token = getToken())
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.productsList

                    val newValue: Int = params.key.toInt() + 1

                    callback.onResult(responseItems, productsRequest.page + 1)
                    networkState.postValue(NetworkState.LOADED)

                } else {

                    retry = {
                        loadAfter(params, callback)
                    }

                    networkState.postValue(
                        NetworkState.error(getErrorMessage(response))
                    )
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(
                    NetworkState.error(t.message ?: "unknown err")
                )

            }
        })


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        networkState.postValue(NetworkState.LOADING)
        productsRequest.page = params.key.toInt()
        val call =
            apiService.getProductsNormal(productsRequest = productsRequest, token = getToken())
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.productsList

                    callback.onResult(responseItems, params.key)
                    networkState.postValue(NetworkState.LOADED)

                } else {
                    retry = {
                        loadBefore(params, callback)
                    }
                    networkState.postValue(
                        NetworkState.error(getErrorMessage(response))
                    )
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                retry = {
                    loadBefore(params, callback)
                }
                networkState.postValue(
                    NetworkState.error(t.message ?: "unknown err")
                )
            }
        })
    }

    protected fun getToken(): String {
        return "$BEARER${PreferenceHelper.getValue(
            PreferenceHelper.ACCESS_TOKEN,
            "",
            MyApp.getAppContext()
        ) as String}"
    }
}
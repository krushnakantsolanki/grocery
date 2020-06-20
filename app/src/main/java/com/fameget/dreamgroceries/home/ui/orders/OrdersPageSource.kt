package com.fameget.dreamgroceries.home.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.data.*
import com.fameget.dreamgroceries.utilities.BEARER
import com.fameget.dreamgroceries.utilities.PreferenceHelper
import com.fameget.dreamgroceries.utilities.Utils
import com.fameget.dreamgroceries.webservices.ApiService
import com.fameget.dreamgroceries.webservices.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


public class OrdersPageSource(val pageReq: PageReq) :
    PageKeyedDataSource<Int, Order>() {

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
        callback: LoadInitialCallback<Int, Order>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        pageReq.page = 1
        val call =
            apiService.getOrdersCall2(token = getToken(), pageReq = pageReq)
        call.enqueue(object : Callback<OrdersResponse> {
            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.data
                    networkState.postValue(NetworkState.LOADED)
                    if (responseItems.isNullOrEmpty())
                        initialLoad.postValue(NetworkState.EMPTY)
                    else
                        initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(responseItems, null, pageReq.page + 1)

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

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                val error = NetworkState.error(t.message ?: "unknown error")
                retry = {
                    loadInitial(params, callback)
                }
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        })


    }

    private fun getErrorMessage(response: Response<OrdersResponse>): String? {
        response.errorBody()?.let {
            val message = Utils.getErrorFromErrorBody(it)
            if (message.isNotEmpty())
                return message

        }
        return " ${response.code()} ${response.message()}"
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Order>) {
        networkState.postValue(NetworkState.LOADING)
        pageReq.page = params.key.toInt()
        val call =
            apiService.getOrdersCall2(getToken(), pageReq)
        call.enqueue(object : Callback<OrdersResponse> {
            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.data

                    val newValue: Int = params.key.toInt() + 1

                    callback.onResult(responseItems, pageReq.page + 1)
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

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(
                    NetworkState.error(t.message ?: "unknown err")
                )

            }
        })


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Order>) {
        networkState.postValue(NetworkState.LOADING)
        pageReq.page = params.key.toInt()
        val call =
            apiService.getOrdersCall2(getToken(), pageReq)
        call.enqueue(object : Callback<OrdersResponse> {
            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.data

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

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
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
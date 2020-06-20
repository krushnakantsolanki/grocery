package com.fameget.dreamgroceries.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.fameget.dreamgroceries.webservices.Resource
import com.fameget.dreamgroceries.webservices.Status
import kotlinx.coroutines.Dispatchers

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Result.Status.SUCCESS] - with data from database
 * [Result.Status.ERROR] - if error has occurred from any source
 * [Result.Status.LOADING]
 */
/*
fun <T, A> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == Status.ERROR) {
            //emit(Resource.error<T>(responseStatus.message!!))
            emit(
                Resource.error(
                    data = null,
                    message = responseStatus.message!!
                )
            )
            emitSource(source)
        }
    }
*/


fun <T> resultLiveDataNoDb(
    networkCall: suspend () -> Resource<T>

    ): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Status.SUCCESS) {
            emit(Resource.success(responseStatus.data) as Resource<T>)
        } else if (responseStatus.status == Status.ERROR) {
            emit(
                Resource.error(
                    data = null,
                    message = responseStatus.message!!,
                    errorCode = responseStatus.errorCode

                )
            )
        }
    }
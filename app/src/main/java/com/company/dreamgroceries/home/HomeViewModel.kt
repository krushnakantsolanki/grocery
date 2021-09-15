package com.company.dreamgroceries.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.company.dreamgroceries.data.BaseResponse
import com.company.dreamgroceries.data.NotCountResponse
import com.company.dreamgroceries.webservices.Resource

class HomeViewModel(val homeRepo :HomeRepo) :ViewModel() {
    fun getNotificationCount() : LiveData<Resource<NotCountResponse>> {
        return homeRepo.getNotCount()
    }
}
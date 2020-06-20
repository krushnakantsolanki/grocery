package com.fameget.dreamgroceries.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fameget.dreamgroceries.data.BaseResponse
import com.fameget.dreamgroceries.data.NotCountResponse
import com.fameget.dreamgroceries.webservices.Resource

class HomeViewModel(val homeRepo :HomeRepo) :ViewModel() {
    fun getNotificationCount() : LiveData<Resource<NotCountResponse>> {
        return homeRepo.getNotCount()
    }
}
package com.fameget.dreamgroceries.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fameget.dreamgroceries.ProductsRepo
import com.fameget.dreamgroceries.addorder.ManualOrderRepo
import com.fameget.dreamgroceries.addorder.ManualOrderViewModel
import com.fameget.dreamgroceries.addorder.ProductsViewModel
import com.fameget.dreamgroceries.home.HomeRepo
import com.fameget.dreamgroceries.home.HomeViewModel
import com.fameget.dreamgroceries.home.ui.orders.OrderDetailRepo
import com.fameget.dreamgroceries.home.ui.orders.OrderDetailViewModel
import com.fameget.dreamgroceries.home.ui.orders.OrdersRepo
import com.fameget.dreamgroceries.home.ui.orders.OrdersViewModel
import com.fameget.dreamgroceries.home.ui.profile.AddressRepo
import com.fameget.dreamgroceries.home.ui.profile.AddressViewModel
import com.fameget.dreamgroceries.home.ui.profile.ProfileRepo
import com.fameget.dreamgroceries.home.ui.profile.ProfileViewModel
import com.fameget.dreamgroceries.login.LoginRepository
import com.fameget.dreamgroceries.login.LoginViewModel
import com.fameget.dreamgroceries.notification.NotificationRepo
import com.fameget.dreamgroceries.notification.NotificationViewModel

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepository()) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(ProfileRepo()) as T
        } else if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            return ProductsViewModel(ProductsRepo()) as T
        } else if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(AddressRepo()) as T
        } else if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(OrdersRepo()) as T
        } else if (modelClass.isAssignableFrom(ManualOrderViewModel::class.java)) {
            return ManualOrderViewModel(ManualOrderRepo()) as T
        } else if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            return OrderDetailViewModel(OrderDetailRepo()) as T
        } else if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(NotificationRepo()) as T
        }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(HomeRepo()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}


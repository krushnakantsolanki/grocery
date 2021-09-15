package com.company.dreamgroceries.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.dreamgroceries.ProductsRepo
import com.company.dreamgroceries.addorder.ManualOrderRepo
import com.company.dreamgroceries.addorder.ManualOrderViewModel
import com.company.dreamgroceries.addorder.ProductsViewModel
import com.company.dreamgroceries.home.HomeRepo
import com.company.dreamgroceries.home.HomeViewModel
import com.company.dreamgroceries.home.ui.orders.OrderDetailRepo
import com.company.dreamgroceries.home.ui.orders.OrderDetailViewModel
import com.company.dreamgroceries.home.ui.orders.OrdersRepo
import com.company.dreamgroceries.home.ui.orders.OrdersViewModel
import com.company.dreamgroceries.home.ui.profile.AddressRepo
import com.company.dreamgroceries.home.ui.profile.AddressViewModel
import com.company.dreamgroceries.home.ui.profile.ProfileRepo
import com.company.dreamgroceries.home.ui.profile.ProfileViewModel
import com.company.dreamgroceries.login.LoginRepository
import com.company.dreamgroceries.login.LoginViewModel
import com.company.dreamgroceries.notification.NotificationRepo
import com.company.dreamgroceries.notification.NotificationViewModel

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


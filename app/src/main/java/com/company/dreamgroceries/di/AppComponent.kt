package com.company.dreamgroceries.di

import com.company.dreamgroceries.ProductsRepo
import com.company.dreamgroceries.addorder.ManualOrderRepo
import com.company.dreamgroceries.home.HomeRepo
import com.company.dreamgroceries.home.ui.orders.OrderDetailRepo
import com.company.dreamgroceries.home.ui.orders.OrdersPageSource
import com.company.dreamgroceries.home.ui.orders.OrdersRepo
import com.company.dreamgroceries.home.ui.orders.ProductPageSource
import com.company.dreamgroceries.home.ui.profile.AddressRepo
import com.company.dreamgroceries.home.ui.profile.ProfileRepo
import com.company.dreamgroceries.login.LoginViewModel
import com.company.dreamgroceries.login.LoginRepository
import com.company.dreamgroceries.notification.NotificationPageSource
import com.company.dreamgroceries.notification.NotificationRepo
import com.company.dreamgroceries.webservices.NetworkInterceptor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface AppComponent {

    fun inject(loginRepository: LoginRepository)

    fun inject(networkInterceptor: NetworkInterceptor)


    fun inject(loginViewModel: LoginViewModel)
    fun inject(productsRepo: ProductsRepo)
    fun inject(addressRepo: AddressRepo)
    fun inject(ordersRepo: OrdersRepo)
    fun inject(manualOrderRepo: ManualOrderRepo)
    fun inject(productPageSource: ProductPageSource)
    fun inject(profileRepo: ProfileRepo)
    fun inject(orderDetailRepo: OrderDetailRepo)
    fun inject(ordersPageSource: OrdersPageSource)
    fun inject(notificationPageSource: NotificationPageSource)
    fun inject(homeRepo: HomeRepo)
    fun inject(notificationRepo: NotificationRepo)


}
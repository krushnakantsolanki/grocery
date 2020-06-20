package com.fameget.dreamgroceries.di

import com.fameget.dreamgroceries.ProductsRepo
import com.fameget.dreamgroceries.addorder.ManualOrderRepo
import com.fameget.dreamgroceries.home.HomeRepo
import com.fameget.dreamgroceries.home.ui.orders.OrderDetailRepo
import com.fameget.dreamgroceries.home.ui.orders.OrdersPageSource
import com.fameget.dreamgroceries.home.ui.orders.OrdersRepo
import com.fameget.dreamgroceries.home.ui.orders.ProductPageSource
import com.fameget.dreamgroceries.home.ui.profile.AddressRepo
import com.fameget.dreamgroceries.home.ui.profile.ProfileRepo
import com.fameget.dreamgroceries.login.LoginViewModel
import com.fameget.dreamgroceries.login.LoginRepository
import com.fameget.dreamgroceries.notification.NotificationPageSource
import com.fameget.dreamgroceries.notification.NotificationRepo
import com.fameget.dreamgroceries.webservices.NetworkInterceptor
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
package com.fameget.dreamgroceries.webservices

import com.fameget.dreamgroceries.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("send-otp")
    suspend fun getOtp(@Body otpReq: OtpRequest): Response<OtpResponse>


    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("login-social")
    suspend fun socialMediaSignIn(@Body socialLoginRequest: SocialLoginRequest): Response<SocialLoginResponse>

    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("auth/token")
    suspend fun login(@Body loginRequest: LoginRequest): Response<SocialLoginResponse>

    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("user")
    suspend fun registerUser(
        @Body registerUserRequest: UserRegRequest,
        @Header("Authorization") value: String
    ): Response<SocialLoginResponse>

    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @GET("get-categories")
    suspend fun getCategories(@Header("Authorization") value: String): Response<CategoryResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("address/update")
    suspend fun updateAddress(
        @Body addresse: Addresse,
        @Header("Authorization") value: String
    ): Response<AddressResponse>


    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Body productsRequest: ProductsRequest
    ): Response<ProductsResponse>


    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("products")
    fun getProductsNormal(
        @Header("Authorization") token: String,
        @Body productsRequest: ProductsRequest
    ): Call<ProductsResponse>

    @Headers("uuid: n6hVV8uBzGsgBSMG")
    @POST("address/remove")
    suspend fun deleteAddress(
        @Body addresse: DeleteAddressReq,
        @Header("Authorization") token: String
    ): Response<AddressResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("shopping-cart/add")
    suspend fun addProductsToCartCall(
        @Header("Authorization") token: String,
        @Body cartProducts: AddToCartReq
    ): Response<BaseResponse>


    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("order-place")
    suspend fun placeBrowseDeliveryCODOrderCall(
        @Header("Authorization") token: String,
        @Body browseDeliveryCODReq: BrowseDelOrderReq
    ): Response<PlaceOrderResponse>


    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("order-place")
    suspend fun placeBrowseDeliveryPickUpOrderCall(
        @Header("Authorization") token: String,
        @Body browsePickUpOrderReq: BrowsePickUpOrderReq
    ): Response<PlaceOrderResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("orders-list")
    suspend fun getOrdersCall(
        @Header("Authorization") token: String,
        @Body pageReq: PageReq
    ): Response<OrdersResponse>


    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("orders-list")
    fun getOrdersCall2(
        @Header("Authorization") token: String,
        @Body pageReq: PageReq
    ): Call<OrdersResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("order-cancel")
    suspend fun cancelOrderCall(
        @Header("Authorization") token: String,
        @Body cancelOrderReq: CancelOrderReq
    ): Response<BaseResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("order-place")
    suspend fun manualPickOrderCall(
        @Header("Authorization") token: String,
        @Body createPickUpOrderReq: ManualPickUpOrderReq
    ): Response<PlaceOrderResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("order-place")
    suspend fun placeDelOrderManual(
        @Body manualDelOrderReq: ManualDelOrderReq,
        @Header("Authorization") token: String
    ): Response<PlaceOrderResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("user")
    suspend fun updateProfile(
        @Body updateProfileReq: UpdateProfileReq,
        @Header("Authorization") token: String
    ): Response<SocialLoginResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("user")
    suspend fun updateProfile2(
        @Body updateProfileReq: UpdateProfilePhone,
        @Header("Authorization") token: String
    ): Response<SocialLoginResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("orders-details")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Body orderReq: OrderReq
    ): Response<OrderResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("notification-list")
    fun getNotifications(
        @Body pageReq: PageReq,
        @Header("Authorization") token: String
    ): Call<NotificationResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("user")
    suspend fun updateNotificationSettingsCall(
        @Body updateNotificationReq: UpdateNotificationReq,
        @Header("Authorization") token: String
    ): Response<BaseResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @GET("notification-list")
    suspend fun getNotCount(@Header("Authorization") token: String): Response<NotCountResponse>

    @Headers(
        "uuid: n6hVV8uBzGsgBSMG",
        "X-localization:sp"
    )
    @POST("notification-remove")
    suspend fun clearNotifications(
        @Header("Authorization") token: String,
        @Body idReq: IdReq
    ): Response<BaseResponse>


}
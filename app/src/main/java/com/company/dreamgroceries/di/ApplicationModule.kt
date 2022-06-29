package com.company.dreamgroceries.di

import android.content.Context
import com.company.dreamgroceries.webservices.ApiService
import com.company.dreamgroceries.webservices.NetworkInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val BASE_URL = "http://35.154.108.175/ecommerce/api/v1/"


@Module
class ApplicationModule {


    @Provides
    @Singleton
    fun provideLoginRetrofitService(): ApiService {
        // Whenever Dagger needs to provide an instance of type LoginRetrofitService,
        // this code (the one inside the @Provides method) is run.

        val httpClient = OkHttpClient.Builder()

     //   if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)

    //    }
        httpClient.addInterceptor(NetworkInterceptor())

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())

            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun getAppContext(): Context {
        return getAppContext()
    }


}
package com.binarybricks.coinhood.network.api

import com.binarybricks.coinhood.BuildConfig
import com.binarybricks.coinhood.network.BASE_CRYPTOCOMPARE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by pranay airan on 1/8/18.
 * api provider for crypto compare and others.
 */


val cryptoCompareRetrofit: Retrofit by lazy {
    Retrofit.Builder()
            .baseUrl(BASE_CRYPTOCOMPARE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

val okHttpClient: OkHttpClient by lazy {
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
    }

    builder.build()
}
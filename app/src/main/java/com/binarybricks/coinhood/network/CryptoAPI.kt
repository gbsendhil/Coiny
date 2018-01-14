package com.binarybricks.coinhood.network

import com.binarybricks.coinhood.BuildConfig
import com.binarybricks.coinhood.CoinHoodApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by pairan on 1/8/18.
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
    val cacheSize: Long = 10 * 1024 * 1024 // 10 MB
    val cache = Cache(CoinHoodApplication.getGlobalAppContext().cacheDir, cacheSize)

    val builder = OkHttpClient.Builder()
    builder.cache(cache)

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
    }

    builder.build()
}
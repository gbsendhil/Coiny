package com.binarybricks.coinhood.components.cryptonewsmodule

import com.binarybricks.coinhood.network.api.API
import com.binarybricks.coinhood.network.api.cryptoCompareRetrofit
import com.binarybricks.coinhood.network.models.CryptoPanicNews
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import io.reactivex.Single

/**
 * Created by Pragya Agrawal
 * Repository that interact with crypto api to get news.
 */

class CryptoNewsRepository(private val baseSchedulerProvider: BaseSchedulerProvider) {

    /**
     * Get the top news for specific coin from cryptopanic
     */
    fun getCryptoPanicNews(coinSymbol: String): Single<CryptoPanicNews> {
        return cryptoCompareRetrofit.create(API::class.java)
                .getCryptoNewsForCurrency(coinSymbol, "important")
                .subscribeOn(baseSchedulerProvider.io())
    }
}
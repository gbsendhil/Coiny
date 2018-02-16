package com.binarybricks.coiny.components.cryptonewsmodule

import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.CryptoPanicNews
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
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

        return if (CoinyCache.newsMap.containsKey(coinSymbol)) {
            Single.just(CoinyCache.newsMap[coinSymbol])
        } else {

            cryptoCompareRetrofit.create(API::class.java)
                .getCryptoNewsForCurrency(coinSymbol, "important", true)
                .subscribeOn(baseSchedulerProvider.io())
                .doOnSuccess {
                    CoinyCache.newsMap[coinSymbol] = it
                }
        }
    }
}
package com.binarybricks.coinhood.components.cryptonewsmodule

import com.binarybricks.coinhood.data.CoinHoodCache
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

        return if (CoinHoodCache.newsMap.containsKey(coinSymbol)) {
            Single.just(CoinHoodCache.newsMap[coinSymbol])
        } else {

            cryptoCompareRetrofit.create(API::class.java)
                    .getCryptoNewsForCurrency(coinSymbol, "important")
                    .subscribeOn(baseSchedulerProvider.io())
                    .doOnSuccess {
                        CoinHoodCache.newsMap[coinSymbol] = it
                    }
        }
    }
}
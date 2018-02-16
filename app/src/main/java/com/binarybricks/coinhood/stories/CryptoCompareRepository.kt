package com.binarybricks.coinhood.stories

import com.binarybricks.coinhood.data.CoinHoodCache
import com.binarybricks.coinhood.network.api.API
import com.binarybricks.coinhood.network.api.cryptoCompareRetrofit
import com.binarybricks.coinhood.network.models.CCCoin
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.getCoinPricesFromJson
import com.binarybricks.coinhood.utils.getCoinsFromJson
import com.binarybricks.coinhood.utils.getExchangeListFromJson
import io.reactivex.Single
import timber.log.Timber
import java.util.*

/**
Created by Pranay Airan 1/8/18.
 * Repository that interact with crypto api to get any info on coins.
 */

class CryptoCompareRepository(private val baseSchedulerProvider: BaseSchedulerProvider) {

    /**
     * Get list of all supported coins
     */
    fun getAllCoins(): Single<ArrayList<CCCoin>> {
        return cryptoCompareRetrofit.create(API::class.java)
            .getCoinList()
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Coin fetched, parsing response")
                getCoinsFromJson(it)
            }
    }

    /**
     * Get Price of Single from currency to single to currency, we are using the same api for multi.
     */
    fun getCoinPrice(fromCurrencySymbol: String, toCurrencySymbol: String): Single<CoinPrice?> {
        return if (CoinHoodCache.coinPriceMap.containsKey(fromCurrencySymbol)) {
            Single.just(CoinHoodCache.coinPriceMap[fromCurrencySymbol])
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                .getPricesFull(fromCurrencySymbol, toCurrencySymbol)
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Coin price fetched, parsing response")
                    val coinPriceList = getCoinPricesFromJson(it)
                    if (coinPriceList.size > 0) {
                        CoinHoodCache.coinPriceMap[fromCurrencySymbol] = coinPriceList[0]
                        coinPriceList[0]
                    } else {
                        null
                    }
                }
        }
    }

    /**
     * Get price of all currencies with respect to a specific currency
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example btc,eth.[toCurrencySymbol]
     * is which currency you want data in for like USD
     */
    fun getCoinPriceFull(fromCurrencySymbol: String, toCurrencySymbol: String): Single<ArrayList<CoinPrice>> {

        return cryptoCompareRetrofit.create(API::class.java)
            .getPricesFull(fromCurrencySymbol, toCurrencySymbol)
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Coin price fetched, parsing response")
                getCoinPricesFromJson(it)
            }
    }


    /**
     * Get list of all supported exchanges
     */
    fun getAllSupportedExchanges(): Single<ArrayList<String>> {
        return cryptoCompareRetrofit.create(API::class.java)
            .getExchangeList()
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Exchanges fetched, parsing response")
                getExchangeListFromJson(it)
            }
    }
}
package com.binarybricks.coinhood.stories

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
 * Created by pranay airan on 1/8/18.
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
     * Get the historical data for specific crypto currencies. [period] specifies what time period you
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example bitcoin.[toCurrencySymbol]
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
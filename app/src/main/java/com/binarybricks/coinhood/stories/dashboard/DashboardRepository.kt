package com.binarybricks.coinhood.stories.dashboard

import com.binarybricks.coinhood.data.database.CoinHoodDatabase
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.api.API
import com.binarybricks.coinhood.network.api.cryptoCompareRetrofit
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.getCoinPricesFromJson
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.util.*

/**
 * Created by pranay airan on 1/8/18.
 * Repository that interact with crypto api and database for getting data.
 */

class DashboardRepository(private val baseSchedulerProvider: BaseSchedulerProvider,
                          private val coinHoodDatabase: CoinHoodDatabase?) {

    /**
     * Get list of all supported coins
     */
    fun loadWatchedCoins(): Flowable<List<WatchedCoin>>? {

        coinHoodDatabase?.let {
            return it.watchedCoinDao().getAllWatchedCoins().subscribeOn(baseSchedulerProvider.io())
        }
        return null
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
                Timber.d("Coin prices fetched, parsing response")
                getCoinPricesFromJson(it)
            }
    }
}
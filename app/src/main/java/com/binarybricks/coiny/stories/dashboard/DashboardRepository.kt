package com.binarybricks.coiny.stories.dashboard

import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.getCoinPricesFromJson
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.util.*

/**
 Created by Pranay Airan 1/8/18.
 * Repository that interact with crypto api and database for getting data.
 */

class DashboardRepository(private val baseSchedulerProvider: BaseSchedulerProvider,
                          private val coinyDatabase: CoinyDatabase?) {

    /**
     * Get list of all coins that is added in watch list
     */
    fun loadWatchedCoins(): Flowable<List<WatchedCoin>>? {

        coinyDatabase?.let {
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

    /**
     * Get list of all supported coins
     */
    fun loadSupportedCoins(): Flowable<List<Coin>>? {
        coinyDatabase?.let {
            return it.coinDao().getAllCoins().subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }
}
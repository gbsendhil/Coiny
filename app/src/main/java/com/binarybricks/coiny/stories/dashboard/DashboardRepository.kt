package com.binarybricks.coiny.stories.dashboard

import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.getCoinPricesFromJson
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber

/**
Created by Pranay Airan
 * Repository that interact with crypto api and database for getting data.
 */

class DashboardRepository(
    private val baseSchedulerProvider: BaseSchedulerProvider,
    private val coinyDatabase: CoinyDatabase?
) {

    /**
     * Get list of all coins that is added in watch list
     */
    fun loadWatchedCoins(): Flowable<List<WatchedCoin>>? {
        coinyDatabase?.let {
            return it.watchedCoinDao().getAllWatchedCoins()
                    .subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }

    /**
     * Get list of all coin transactions
     */
    fun loadTransactions(): Flowable<List<CoinTransaction>>? {

        coinyDatabase?.let {
            return it.coinTransactionDao().getAllCoinTransaction()
                    .subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }

    /**
     * Get the price of a coin from the API
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example bitcoin.
     * [toCurrencySymbol] is which currency you want data in for like USD
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
package com.binarybricks.coinhood.stories.coindetails

import com.binarybricks.coinhood.data.database.CoinHoodDatabase
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import io.reactivex.Flowable

/**
Created by Pranay Airan 1/8/18.
 * Repository that interact with crypto api and database for getting data.
 */

class AllCoinDetailsRepository(private val baseSchedulerProvider: BaseSchedulerProvider,
                               private val coinHoodDatabase: CoinHoodDatabase?) {

    /**
     * Get list of all coins that is added in watch list
     */
    fun loadWatchedCoins(): Flowable<List<WatchedCoin>>? {

        coinHoodDatabase?.let {
            return it.watchedCoinDao().getAllWatchedCoins().subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }
}
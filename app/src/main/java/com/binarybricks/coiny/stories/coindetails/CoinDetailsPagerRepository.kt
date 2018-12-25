package com.binarybricks.coiny.stories.coindetails

import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import io.reactivex.Single

/**
Created by Pranay Airan
 * Repository that interact with crypto api and database for getting data.
 */

class CoinDetailsPagerRepository(
    private val baseSchedulerProvider: BaseSchedulerProvider,
    private val coinyDatabase: CoinyDatabase?
) {

    /**
     * Get list of all coins that is added in watch list
     */
    fun loadWatchedCoins(): Single<List<WatchedCoin>>? {

        coinyDatabase?.let {
            return it.watchedCoinDao().getAllWatchedCoinsOnetime().subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }
}
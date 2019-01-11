package com.binarybricks.coiny.components.cointickermodule

import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.Exchange
import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.CryptoTicker
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.getCoinTickerFromJson
import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.functions.BiFunction

/**
 * Created by Pranay Airan
 * Repository that interact with coin gecko api to get ticker info.
 */

class CoinTickerRepository(
    private val baseSchedulerProvider: BaseSchedulerProvider,
    private val coinyDatabase: CoinyDatabase?
) {

    /**
     * Get the ticker info from coin gecko
     */
    fun getCryptoTickers(coinName: String): Single<List<CryptoTicker>> {

        return if (CoinyCache.ticker.containsKey(coinName)) {
            Single.just(CoinyCache.ticker[coinName])
        } else {

            Single.zip(cryptoCompareRetrofit.create(API::class.java).getCoinTicker(coinName),
                    loadExchangeList(), BiFunction { t1: JsonObject, t2: List<Exchange>? ->
                Pair(t1, t2)
            }).map { getCoinTickerFromJson(it.first, it.second) }
                    .subscribeOn(baseSchedulerProvider.io())
                    .doOnSuccess {
                        if (it.isNotEmpty()) {
                            CoinyCache.ticker[coinName] = it
                        }
                    }
        }
    }

    /**
     * Get list of all exchanges, this is needed for logo
     */
    private fun loadExchangeList(): Single<List<Exchange>>? {
        coinyDatabase?.let {
            return it.exchangeDao().getAllExchanges()
                    .subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }
}
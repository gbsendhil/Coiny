package com.binarybricks.coinhood.stories

import com.binarybricks.coinhood.network.cryptoCompareRetrofit
import com.binarybricks.coinhood.network.models.Coin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.getCoinsFromJson
import com.rmnivnv.cryptomoon.model.network.CryptoCompareAPI
import io.reactivex.Single
import timber.log.Timber
import java.util.*

/**
 * Created by pranay airan on 1/8/18.
 * Repository that interact with crypto api to get any info on coins.
 */

class CoinRepository(private val baseSchedulerProvider: BaseSchedulerProvider) {

    /**
     * Get the historical data for specific crypto currencies. [period] specifies what time period you
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example bitcoin.[toCurrencySymbol]
     * is which currency you want data in for like USD
     */
    fun getSingleCoinPrice(fromCurrencySymbol: String, toCurrencySymbol: String): Single<ArrayList<Coin>> {

        return cryptoCompareRetrofit.create(CryptoCompareAPI::class.java)
                .getPrices(fromCurrencySymbol, toCurrencySymbol)
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Coin price fetched, parsing response")
                    getCoinsFromJson(it)
                }
    }

}
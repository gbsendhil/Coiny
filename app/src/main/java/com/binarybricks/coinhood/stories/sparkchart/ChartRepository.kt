package com.binarybricks.coinhood.stories.sparkchart

import com.binarybricks.coinhood.network.*
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.rmnivnv.cryptomoon.model.network.CryptoCompareAPI
import timber.log.Timber

/**
 * Created by pairan on 1/8/18.
 * Repository that interact with crypto api to get charts.
 */

class ChartRepository(private val baseSchedulerProvider: BaseSchedulerProvider) {

    /**
     * Get the historical data for specific crypto currencies. [period] specifies what time period you
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example bitcoin.[toCurrencySymbol]
     * is which currency you want data in for like USD
     */
    fun getCryptoHistoricalData(period: String, fromCurrencySymbol: String?, toCurrencySymbol: String?) {

        val histoPeriod: String
        var limit = 30
        var aggregate = 1
        when (period) {
            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 2 // this pulls for 2 hours
            }
            HOURS12 -> {
                histoPeriod = HISTO_HOUR
                limit = 12
            }
            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24
            }
            DAYS3 -> {
                histoPeriod = HISTO_HOUR
                aggregate = 2
            }
            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6
            }
            MONTH -> {
                histoPeriod = HISTO_DAY
            }
            MONTHS3 -> {
                histoPeriod = HISTO_DAY
                aggregate = 3
            }
            MONTHS6 -> {
                histoPeriod = HISTO_DAY
                aggregate = 6
            }
            else -> {
                histoPeriod = HISTO_DAY
                aggregate = 12
            }
        }

        cryptoCompareRetrofit.create(CryptoCompareAPI::class.java)
                .getCryptoHistoricalData(histoPeriod, fromCurrencySymbol, toCurrencySymbol, limit, aggregate)
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Size of response " + it.size())
                }.observeOn(baseSchedulerProvider.ui()).subscribe()
    }

}
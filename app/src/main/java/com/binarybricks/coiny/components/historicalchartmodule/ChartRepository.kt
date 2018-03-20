package com.binarybricks.coiny.components.historicalchartmodule

import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.CryptoCompareHistoricalResponse
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.network.ALL
import com.binarybricks.coiny.network.HISTO_DAY
import com.binarybricks.coiny.network.HISTO_HOUR
import com.binarybricks.coiny.network.HISTO_MINUTE
import com.binarybricks.coiny.network.HOUR
import com.binarybricks.coiny.network.HOURS24
import com.binarybricks.coiny.network.MONTH
import com.binarybricks.coiny.network.WEEK
import com.binarybricks.coiny.network.YEAR
import io.reactivex.Single
import timber.log.Timber

/**
 Created by Pranay Airan
 * Repository that interact with crypto api to get charts.
 */

class ChartRepository(private val baseSchedulerProvider: BaseSchedulerProvider) {

    /**
     * Get the historical data for specific crypto currencies. [period] specifies what time period you
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example bitcoin.[toCurrencySymbol]
     * is which currency you want data in for like USD
     */
    fun getCryptoHistoricalData(period: String, fromCurrencySymbol: String?, toCurrencySymbol: String?): Single<Pair<List<CryptoCompareHistoricalResponse.Data>, CryptoCompareHistoricalResponse.Data?>> {

        val histoPeriod: String
        var limit = 30
        var aggregate = 1
        when (period) {
            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 1 // this pulls for 1 hour
            }
            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24 // 1 day
            }
            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6 //1 week limit is 128 hours default that is
            }
            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30 // 30 days
            }
            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13 // default limit is 30 so 30*12 365 days
            }
            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }
            else -> {
                histoPeriod = HISTO_HOUR
                limit = 24 // 1 day
            }
        }

        return cryptoCompareRetrofit.create(API::class.java)
                .getCryptoHistoricalData(histoPeriod, fromCurrencySymbol, toCurrencySymbol, limit, aggregate)
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Size of response " + it.data.size)
                    val maxClosingValueFromHistoricalData = it.data.maxBy { it.close.toFloat() }
                    Pair(it.data, maxClosingValueFromHistoricalData)
                }
    }

}
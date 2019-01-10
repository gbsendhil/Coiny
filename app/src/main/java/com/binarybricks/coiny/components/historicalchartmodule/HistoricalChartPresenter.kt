package com.binarybricks.coiny.components.historicalchartmodule

import HistoricalChartContract
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import timber.log.Timber

/**
Created by Pranay Airan
 */

class HistoricalChartPresenter(private val schedulerProvider: BaseSchedulerProvider, private val chartRepo: ChartRepository)
    : BasePresenter<HistoricalChartContract.View>(), HistoricalChartContract.Presenter {

    /**
     * Load historical data for the coin to show the chart.
     */
    override fun loadHistoricalData(period: String, fromCurrency: String, toCurrency: String) {
        currentView?.showOrHideChartLoadingIndicator(true)

        compositeDisposable.add(chartRepo.getCryptoHistoricalData(period, fromCurrency, toCurrency)
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate { currentView?.showOrHideChartLoadingIndicator(false) }
                .subscribe({
                    currentView?.onHistoricalDataLoaded(period, it)
                }, {
                    Timber.e(it.localizedMessage)
                }))
    }
}
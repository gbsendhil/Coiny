package com.binarybricks.coinhood.components.historicalchart

import HistoricalChartContract
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.BasePresenter
import timber.log.Timber

/**
 * Created by pranay airan on 1/17/18.
 */

class HistoricalChartPresenter(private val schedulerProvider: BaseSchedulerProvider) : BasePresenter<HistoricalChartContract.View>(), HistoricalChartContract.Presenter {

    private val chatRepo by lazy {
        ChartRepository(schedulerProvider)
    }

    /**
     * Load historical data for the coin to show the chart.
     */
    override fun loadHistoricalData(period: String, fromCurrency: String, toCurrency: String) {
        currentView?.showOrHideChartLoadingIndicator(true)

        compositeDisposable.add(chatRepo.getCryptoHistoricalData(period, fromCurrency, toCurrency)
                .filter { it.first.isNotEmpty() }
                .observeOn(schedulerProvider.ui())
                .doFinally({ currentView?.showOrHideChartLoadingIndicator(false) })
                .subscribe({
                    currentView?.onHistoricalDataLoaded(period, it)
                }, {
                    Timber.e(it.localizedMessage)
                }))
    }
}
package com.binarybricks.coinhood.stories.sparkchart

import HistoricalChartContract
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.BasePresenter
import com.binarybricks.coinhood.stories.CoinRepository
import timber.log.Timber

/**
 * Created by pranay airan on 1/17/18.
 */

class HistoricalChartPresenter(private val schedulerProvider: BaseSchedulerProvider) : BasePresenter<HistoricalChartContract.View>(), HistoricalChartContract.Presenter {

    private val chatRepo by lazy {
        ChartRepository(schedulerProvider)
    }

    private val coinRepo by lazy {
        CoinRepository(schedulerProvider)
    }

    /**
     * Get the current price of a coin say btc or eth
     */
    override fun loadCurrentCoinPrice(fromCurrency: String, toCurrency: String) {
        compositeDisposable.add(coinRepo.getSingleCoinPrice(fromCurrency, toCurrency)
                .filter { it.size > 0 }
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    currentView?.addCoinAndAnimateCoinPrice(it[0])
                }, { Timber.e(it.localizedMessage) }))
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
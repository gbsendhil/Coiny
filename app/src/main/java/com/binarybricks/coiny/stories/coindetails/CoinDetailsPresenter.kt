package com.binarybricks.coiny.components.historicalchartmodule

import CoinDetailsContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDetailsPresenter(private val schedulerProvider: BaseSchedulerProvider,
                           private val coinRepo: CryptoCompareRepository) : BasePresenter<CoinDetailsContract.View>()
    , CoinDetailsContract.Presenter, LifecycleObserver {

    /**
     * Get the current price of a coin say btc or eth
     */
    override fun loadCurrentCoinPrice(watchedCoin: WatchedCoin, toCurrency: String) {
        compositeDisposable.add(coinRepo.getCoinPriceFull(watchedCoin.coin.symbol, toCurrency)
            .observeOn(schedulerProvider.ui())
            .subscribe({
                currentView?.onCoinPriceLoaded(it, watchedCoin)
            }, { Timber.e(it.localizedMessage) }))
    }

    override fun loadRecentTransaction(symbol: String) {
        coinRepo.getRecentTransaction(symbol)
            ?.observeOn(schedulerProvider.ui())
            ?.subscribe({ coinTransactionsList ->
                coinTransactionsList?.let {
                    currentView?.onRecentTransactionLoaded(it)
                }
            }, {
                Timber.e(it.localizedMessage)
            })?.let { compositeDisposable.add(it) }
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
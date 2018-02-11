package com.binarybricks.coinhood.components.historicalchartmodule

import CoinDetailsContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.BasePresenter
import com.binarybricks.coinhood.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDetailsPresenter(private val schedulerProvider: BaseSchedulerProvider) : BasePresenter<CoinDetailsContract.View>()
    , CoinDetailsContract.Presenter, LifecycleObserver {

    private val coinRepo by lazy {
        CryptoCompareRepository(schedulerProvider)
    }

    /**
     * Get the current price of a coin say btc or eth
     */
    override fun loadCurrentCoinPrice(watchedCoin: WatchedCoin, toCurrency: String) {
        compositeDisposable.add(coinRepo.getCoinPrice(watchedCoin.coin.symbol, toCurrency)
            .observeOn(schedulerProvider.ui())
            .subscribe({
                currentView?.onCoinPriceLoaded(it, watchedCoin)
            }, { Timber.e(it.localizedMessage) }))
    }


    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
package com.binarybricks.coiny.components.historicalchartmodule

import CoinSearchContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinSearchPresenter(private val schedulerProvider: BaseSchedulerProvider,
                          private val coinRepo: CryptoCompareRepository) : BasePresenter<CoinSearchContract.View>(),
    CoinSearchContract.Presenter, LifecycleObserver {


    override fun loadAllCoins() {
        currentView?.showOrHideLoadingIndicator(true)

        coinRepo.getAllCoins()
            ?.observeOn(schedulerProvider.ui())
            ?.subscribe({
                Timber.d("All Coins Loaded")
                currentView?.showOrHideLoadingIndicator(false)
                currentView?.onCoinsLoaded(it)
            }, {
                currentView?.onNetworkError(it.localizedMessage)
            })?.let { compositeDisposable.add(it) }
    }

    override fun updateCoinWatchedStatus(watched: Boolean, coinID: String, coinSymbol: String) {
        compositeDisposable.add(coinRepo.updateCoinWatchedStatus(watched, coinID)
            .observeOn(schedulerProvider.ui())
            .subscribe({
                Timber.d("Coin status updated")
                currentView?.onCoinWatchedStatusUpdated(watched, coinSymbol)
            }, {
                currentView?.onNetworkError(it.localizedMessage)
            }))
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
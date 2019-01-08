package com.binarybricks.coiny.components.cointickermodule

import CoinTickerContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import timber.log.Timber

/**
 * Created by Pranay Airan
 */

class CoinTickerPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinTickerRepository: CoinTickerRepository
) : BasePresenter<CoinTickerContract.View>(), CoinTickerContract.Presenter, LifecycleObserver {

    /**
     * Load the crypto ticker from the crypto panic api
     */
    override fun getCryptoTickers(coinName: String) {

        var updatedCoinName = coinName

        if (coinName.equals("XRP", true)) {
            updatedCoinName = "ripple"
        }

        currentView?.showOrHideLoadingIndicator(true)

        compositeDisposable.add(coinTickerRepository.getCryptoTickers(updatedCoinName)
                .filter { it.isNotEmpty() }
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate { currentView?.showOrHideLoadingIndicator(false) }
                .subscribe({ currentView?.onPriceTickersLoaded(it) }, {
                    Timber.e(it.localizedMessage)
                    currentView?.onNetworkError("Error loading ticker")
                }))
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
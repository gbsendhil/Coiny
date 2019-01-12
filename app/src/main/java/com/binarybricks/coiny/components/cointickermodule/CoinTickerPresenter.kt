package com.binarybricks.coiny.components.cointickermodule

import CoinTickerContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.R
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.utils.ResourceProvider
import timber.log.Timber

/**
 * Created by Pranay Airan
 */

class CoinTickerPresenter(
    private val schedulerProvider: BaseSchedulerProvider,
    private val coinTickerRepository: CoinTickerRepository,
    private val resourceProvider: ResourceProvider
) : BasePresenter<CoinTickerContract.View>(), CoinTickerContract.Presenter {

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
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate { currentView?.showOrHideLoadingIndicator(false) }
                .subscribe({ currentView?.onPriceTickersLoaded(it) }, {
                    Timber.e(it.localizedMessage)
                    currentView?.onNetworkError(resourceProvider.getString(R.string.error_ticker))
                }))
    }
}
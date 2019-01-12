package com.binarybricks.coiny.stories.coindetails

import CoinDetailsContract
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDetailPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinRepo: CryptoCompareRepository
) : BasePresenter<CoinDetailsContract.View>(),
        CoinDetailsContract.Presenter {

    override fun getWatchedCoinFromSymbol(symbol: String) {

        currentView?.showOrHideLoadingIndicator(true)

        coinRepo.getSingleCoin(symbol)
                ?.observeOn(schedulerProvider.ui())
                ?.subscribe({
                    Timber.d("watched coin loaded")
                    currentView?.showOrHideLoadingIndicator(false)
                    if (it != null) {
                        currentView?.onWatchedCoinLoaded(it.first())
                    } else {
                        currentView?.onWatchedCoinLoaded(null)
                    }
                }, {
                    Timber.e(it)
                    currentView?.onNetworkError(it.localizedMessage)
                })?.let { compositeDisposable.add(it) }
    }
}
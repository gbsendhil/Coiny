package com.binarybricks.coiny.stories.coin

import CoinContract
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinRepo: CryptoCompareRepository
) : BasePresenter<CoinContract.View>(), CoinContract.Presenter {

    /**
     * Get the current price of a coinSymbol say btc or eth
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

    override fun updateCoinWatchedStatus(watched: Boolean, coinID: String, coinSymbol: String) {
        compositeDisposable.add(coinRepo.updateCoinWatchedStatus(watched, coinID)
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("Coin status updated")
                    currentView?.onCoinWatchedStatusUpdated(watched, coinSymbol)
                }, {
                    Timber.e(it)
                    currentView?.onNetworkError(it.localizedMessage)
                }))
    }
}
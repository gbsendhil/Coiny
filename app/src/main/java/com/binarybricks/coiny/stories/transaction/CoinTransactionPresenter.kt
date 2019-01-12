package com.binarybricks.coiny.stories.transaction

import CoinTransactionContract
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinTransactionPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinRepo: CryptoCompareRepository
) : BasePresenter<CoinTransactionContract.View>(), CoinTransactionContract.Presenter {

    override fun getAllSupportedExchanges() {
        compositeDisposable.add(coinRepo.getAllSupportedExchanges()
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("All Exchange Loaded")
                    currentView?.onAllSupportedExchangesLoaded(it)
                }, {
                    Timber.e(it.localizedMessage)
                })
        )
    }

    // to coins is , separated multiple coin list.
    override fun getPriceForPair(fromCoin: String, toCoin: String, exchange: String, timeStamp: String) {
        if (exchange.isNotEmpty()) {
            compositeDisposable.add(coinRepo.getCoinPriceForTimeStamp(fromCoin, toCoin, exchange, timeStamp)
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        Timber.d("Coin price Loaded")
                        currentView?.onCoinPriceLoaded(it)
                    }, {
                        Timber.e(it.localizedMessage)
                    })
            )
        }
    }

    override fun addTransaction(transaction: CoinTransaction) {
        compositeDisposable.add(coinRepo.insertTransaction(transaction)
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("Coin Transaction Added")
                    currentView?.onTransactionAdded()
                }, {
                    Timber.e(it.localizedMessage)
                }))
    }
}
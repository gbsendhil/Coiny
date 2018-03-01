package com.binarybricks.coiny.components.historicalchartmodule

import CoinTransactionContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinTransactionPresenter(private val schedulerProvider: BaseSchedulerProvider,
                               private val coinyDatabase: CoinyDatabase?) :
    BasePresenter<CoinTransactionContract.View>(), CoinTransactionContract.Presenter, LifecycleObserver {

    private val coinRepo by lazy {
        CryptoCompareRepository(schedulerProvider, coinyDatabase)
    }

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

    override fun getPriceForPair(fromCoin: String, toCoin: String, exchange: String) {
        if (exchange.isNotEmpty()) {
            compositeDisposable.add(coinRepo.getCoinPrice(fromCoin, toCoin, exchange)
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
        coinRepo.insertTransaction(transaction)
            .observeOn(schedulerProvider.ui())
            .subscribe({
                Timber.d("Coin Transaction Added")
                currentView?.onTransactionAdded()
            }, {
                Timber.e(it.localizedMessage)
            })
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
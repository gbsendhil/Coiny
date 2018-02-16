package com.binarybricks.coiny.components.historicalchartmodule

import LaunchContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.data.database.entities.Exchange
import com.binarybricks.coiny.network.models.getCoinFromCCCoin
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
 Created by Pranay Airan
 */

class LaunchPresenter(
    private val schedulerProvider: BaseSchedulerProvider,
    private val coinyDatabase: CoinyDatabase?
) : BasePresenter<LaunchContract.View>(), LaunchContract.Presenter, LifecycleObserver {

    private val coinRepo by lazy {
        CryptoCompareRepository(schedulerProvider)
    }

    override fun getAllSupportedCoins() {
        compositeDisposable.add(coinRepo.getAllCoins()
            .filter { it.size > 0 }
            .map {
                val coinList: MutableList<Coin> = mutableListOf()
                it.forEach {
                    coinList.add(getCoinFromCCCoin(it))
                }
                coinyDatabase?.coinDao()
                    ?.insertCoins(coinList)
                coinList
            }
            .observeOn(schedulerProvider.ui())
            .subscribe({
                Timber.d("Inserted all coins in db with size ${it.size}")
            }, {
                Timber.e(it.localizedMessage)
            })
        )
    }

    override fun getAllSupportedExchanges() {
        compositeDisposable.add(coinRepo.getAllSupportedExchanges()
            .filter { it.size > 0 }
            .map {
                val exchangeList: MutableList<Exchange> = mutableListOf()
                it.forEach {
                    exchangeList.add(Exchange(it))
                }
                coinyDatabase?.exchangeDao()
                    ?.insertExchanges(exchangeList)
                exchangeList
            }
            .observeOn(schedulerProvider.ui())
            .subscribe({
                Timber.d("Inserted all exchange with size ${it.size}")
            }, {
                Timber.e(it.localizedMessage)
            })
        )
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
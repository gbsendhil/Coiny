package com.binarybricks.coiny.components.historicalchartmodule

import LaunchContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CCCoin
import com.binarybricks.coiny.network.models.getCoinFromCCCoin
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import com.binarybricks.coiny.stories.getTop5CoinsToWatch
import com.binarybricks.coiny.utils.defaultExchange
import timber.log.Timber

/**
Created by Pranay Airan
 */

class LaunchPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinRepo: CryptoCompareRepository
) : BasePresenter<LaunchContract.View>(), LaunchContract.Presenter, LifecycleObserver {
    private var coinList: ArrayList<CCCoin>? = null

    override fun loadCoinsFromAPIInBackground() {
        compositeDisposable.add(coinRepo.getAllCoinsFromAPI(coinList).subscribe({
            coinList = it.first
        }, { Timber.e(it) }))

        loadExchangeFromAPI()
    }

    private fun loadExchangeFromAPI() {
        compositeDisposable.add(coinRepo.getExchangeInfo()
                .map {
                    compositeDisposable.add(coinRepo.insertExchangeIntoList(it).subscribe())
                }
                .subscribe())
    }

    override fun getAllSupportedCoins(defaultCurrency: String) {
        compositeDisposable.add(coinRepo.getAllCoinsFromAPI(coinList)
                .flatMap {
                    val coinList: MutableList<WatchedCoin> = mutableListOf()
                    val ccCoinList = it.first
                    ccCoinList.forEach { ccCoin ->
                        val coinInfo = it.second[ccCoin.symbol.toLowerCase()]
                        coinList.add(getCoinFromCCCoin(ccCoin, defaultExchange, defaultCurrency, coinInfo))
                    }
                    coinRepo.insertCoinsInWatchList(coinList)
                }.map {
                    // add top 5 coins in watch list
                    val top5CoinsToWatch = getTop5CoinsToWatch()

                    top5CoinsToWatch.forEach {
                        compositeDisposable.add(coinRepo.updateCoinWatchedStatus(true, it)
                                .subscribe())
                    }
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("Loaded all the coins and inserted in DB")
                    currentView?.onAllSupportedCoinsLoaded()
                }, {
                    Timber.e(it.localizedMessage)
                }
                ))
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
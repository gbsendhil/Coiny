package com.binarybricks.coiny.components.historicalchartmodule

import LaunchContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.database.entities.Coin
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

class LaunchPresenter(private val schedulerProvider: BaseSchedulerProvider,
                      private val coinRepo: CryptoCompareRepository) : BasePresenter<LaunchContract.View>(), LaunchContract.Presenter, LifecycleObserver {


    override fun getAllSupportedCoins() {
        compositeDisposable.add(coinRepo.getAllCoins()
            .filter { it.size > 0 }
            .map {
                val coinList: MutableList<Coin> = mutableListOf()
                it.forEach {
                    coinList.add(getCoinFromCCCoin(it))
                }
                coinRepo.insertCoins(coinList)
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

    override fun addTop5CoinsInWishlist(defaultCurrency: String) {
        // add top 5 coins in watch list
        coinRepo.insertCoinsInWatchList(getTop5CoinsToWatch(defaultExchange, defaultCurrency))
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
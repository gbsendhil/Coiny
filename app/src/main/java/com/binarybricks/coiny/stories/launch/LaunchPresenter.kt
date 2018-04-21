package com.binarybricks.coiny.components.historicalchartmodule

import LaunchContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.database.entities.WatchedCoin
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


    override fun loadCoinsFromAPIInBackground() {
        compositeDisposable.add(coinRepo.getAllCoinsFromAPI().subscribe())
    }

    override fun getAllSupportedCoins(defaultCurrency: String) {
        compositeDisposable.add(coinRepo.getAllCoinsFromAPI()
            .filter { it.size > 0 }
            .map {
                val coinList: MutableList<WatchedCoin> = mutableListOf()
                it.forEach {
                    coinList.add(getCoinFromCCCoin(it, defaultExchange, defaultCurrency))
                }

                // insert coins in db
                compositeDisposable.add(coinRepo.insertCoinsInWatchList(coinList)
                    .subscribe({ t1, t2 ->
                        // add top 5 coins in watch list
                        val top5CoinsToWatch = getTop5CoinsToWatch()

                        top5CoinsToWatch.forEach {
                            compositeDisposable.add(coinRepo.updateCoinWatchedStatus(true, it)
                                .subscribe())
                        }
                    }))

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

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
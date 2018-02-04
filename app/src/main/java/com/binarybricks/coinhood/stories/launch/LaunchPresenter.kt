package com.binarybricks.coinhood.components.historicalchartmodule

import LaunchContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coinhood.data.database.CoinHoodDatabase
import com.binarybricks.coinhood.data.database.entities.Coin
import com.binarybricks.coinhood.data.database.entities.Exchange
import com.binarybricks.coinhood.network.models.getCoinFromCCCoin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.BasePresenter
import com.binarybricks.coinhood.stories.CryptoCompareRepository
import timber.log.Timber

/**
 * Created by pranay airan on 1/17/18.
 */

class LaunchPresenter(private val schedulerProvider: BaseSchedulerProvider, private val coinHoodDatabase: CoinHoodDatabase?) : BasePresenter<LaunchContract.View>()
        , LaunchContract.Presenter, LifecycleObserver {

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
                    coinHoodDatabase?.coinDao()?.insertCoins(coinList)
                    coinList
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("Inserted all coins in db with size ${it.size}")
                }, { Timber.e(it.localizedMessage) }))
    }

    override fun getAllSupportedExchanges() {
        compositeDisposable.add(coinRepo.getAllSupportedExchanges()
                .filter { it.size > 0 }
                .map {
                    val exchangeList: MutableList<Exchange> = mutableListOf()
                    it.forEach {
                        exchangeList.add(Exchange(it))
                    }
                    coinHoodDatabase?.exchangeDao()?.insertExchanges(exchangeList)
                    exchangeList
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("Inserted all exchange with size ${it.size}")
                    // put this coin in db
                }, { Timber.e(it.localizedMessage) }))
    }


    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
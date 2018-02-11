package com.binarybricks.coinhood.stories.coindetails

import CoinDetailsPagerContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coinhood.data.database.CoinHoodDatabase
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.BasePresenter
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDetailPagerPresenter(private val schedulerProvider: BaseSchedulerProvider, private val coinHoodDatabase: CoinHoodDatabase?) :
    BasePresenter<CoinDetailsPagerContract.View>(), CoinDetailsPagerContract.Presenter, LifecycleObserver {

    private val allCoinDetailsRepository by lazy {
        AllCoinDetailsRepository(schedulerProvider, coinHoodDatabase)
    }

    override fun loadWatchedCoins() {
        allCoinDetailsRepository.loadWatchedCoins()?.let {
            compositeDisposable.add(
                it.filter { it.isNotEmpty() }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ currentView?.onWatchedCoinsLoaded(it) }, { Timber.e(it.localizedMessage) })
            )
        }
    }


    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
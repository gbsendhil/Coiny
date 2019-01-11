package com.binarybricks.coiny.stories.coindetails

import CoinDetailsPagerContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDetailPagerPresenter(private val schedulerProvider: BaseSchedulerProvider, private val coinDetailsPagerRepository: CoinDetailsPagerRepository) :
        BasePresenter<CoinDetailsPagerContract.View>(), CoinDetailsPagerContract.Presenter, LifecycleObserver {

    override fun loadWatchedCoins() {
        coinDetailsPagerRepository.loadWatchedCoins()?.let {
            compositeDisposable.add(it
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ watchedCoins -> currentView?.onWatchedCoinsLoaded(watchedCoins) },
                            { t -> Timber.e(t.localizedMessage) })
            )
        }
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        detachView()
    }
}
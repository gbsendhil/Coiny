package com.binarybricks.coiny.stories.coindetails

import CoinDetailsPagerContract
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDetailPagerPresenter(private val schedulerProvider: BaseSchedulerProvider, private val coinDetailsPagerRepository: CoinDetailsPagerRepository) :
        BasePresenter<CoinDetailsPagerContract.View>(), CoinDetailsPagerContract.Presenter {

    override fun loadWatchedCoins() {
        coinDetailsPagerRepository.loadWatchedCoins()?.let {
            compositeDisposable.add(it
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ watchedCoins -> currentView?.onWatchedCoinsLoaded(watchedCoins) },
                            { t -> Timber.e(t.localizedMessage) })
            )
        }
    }
}
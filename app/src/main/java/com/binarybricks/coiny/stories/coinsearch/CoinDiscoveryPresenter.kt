package com.binarybricks.coiny.stories.coinsearch

import CoinDiscoveryContract
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDiscoveryPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinRepo: CryptoCompareRepository
) : BasePresenter<CoinDiscoveryContract.View>(),
        CoinDiscoveryContract.Presenter {

    override fun getTopCoinListByMarketCap(toCurrencySymbol: String) {
        compositeDisposable.add(coinRepo.getTopCoinsByTotalVolume(toCurrencySymbol)
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    currentView?.onTopCoinsByTotalVolumeLoaded(it)
                    Timber.d("All Exchange Loaded")
                }, {
                    Timber.e(it.localizedMessage)
                })
        )
    }

    override fun getTopCoinListByPairVolume() {
        compositeDisposable.add(coinRepo.getTopPairsByTotalVolume("BTC")
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    currentView?.onTopCoinListByPairVolumeLoaded(it)
                    Timber.d("Top coins by pair Loaded")
                }, {
                    Timber.e(it.localizedMessage)
                })
        )
    }

    override fun getCryptoCurrencyNews() {
        compositeDisposable.add(coinRepo.getTopNewsFromCryptoCompare()
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    currentView?.onCoinNewsLoaded(it)
                    Timber.d("All news Loaded")
                }, {
                    Timber.e(it.localizedMessage)
                })
        )
    }
}
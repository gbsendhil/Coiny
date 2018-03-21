package com.binarybricks.coiny.components.historicalchartmodule

import CoinDashboardContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import com.binarybricks.coiny.stories.dashboard.DashboardRepository
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDashboardPresenter(private val schedulerProvider: BaseSchedulerProvider,
                             private val dashboardRepository: DashboardRepository,
                             private val coinRepo: CryptoCompareRepository) : BasePresenter<CoinDashboardContract.View>(),
    CoinDashboardContract.Presenter, LifecycleObserver {


    override fun loadWatchedCoins() {
        dashboardRepository.loadWatchedCoins()?.let {
            compositeDisposable.add(
                it.filter { it.isNotEmpty() }
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ currentView?.onWatchedCoinsLoaded(it) }, { Timber.e(it.localizedMessage) })
            )
        }
    }

    override fun loadCoinsPrices(fromCurrencySymbol: String, toCurrencySymbol: String) {
        compositeDisposable.add(dashboardRepository.getCoinPriceFull(fromCurrencySymbol, toCurrencySymbol)
            .filter { it.size > 0 }
            .map { coinPriceList ->
                val coinPriceMap: HashMap<String, CoinPrice> = hashMapOf()
                coinPriceList.forEach { coinPrice ->
                    coinPrice.fromSymbol?.let { fromCurrencySymbol -> coinPriceMap.put(fromCurrencySymbol.toUpperCase(), coinPrice) }
                }
                CoinyCache.coinPriceMap.putAll(coinPriceMap)
                coinPriceMap
            }
            .observeOn(schedulerProvider.ui())
            .subscribe({ currentView?.onCoinPricesLoaded(it) }, { Timber.e(it.localizedMessage) }))
    }

    override fun getAllSupportedExchanges() {
        compositeDisposable.add(coinRepo.getAllSupportedExchanges()
            .observeOn(schedulerProvider.ui())
            .subscribe({
                Timber.d("All Exchange Loaded")
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
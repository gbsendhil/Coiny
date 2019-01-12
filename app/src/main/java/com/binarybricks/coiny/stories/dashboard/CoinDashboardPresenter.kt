package com.binarybricks.coiny.stories.dashboard

import CoinDashboardContract
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import io.reactivex.functions.BiFunction
import timber.log.Timber

/**
Created by Pranay Airan
 */

class CoinDashboardPresenter(
    private val schedulerProvider: BaseSchedulerProvider,
    private val dashboardRepository: DashboardRepository,
    private val coinRepo: CryptoCompareRepository
) : BasePresenter<CoinDashboardContract.View>(),
        CoinDashboardContract.Presenter {

    override fun loadWatchedCoinsAndTransactions() {
        val watchedCoins = dashboardRepository.loadWatchedCoins()
        val transactions = dashboardRepository.loadTransactions()

        if (watchedCoins != null && transactions != null) {

            compositeDisposable.add(watchedCoins.zipWith(transactions, BiFunction<List<WatchedCoin>, List<CoinTransaction>,
                    Pair<List<WatchedCoin>, List<CoinTransaction>>> { watchedCoinList, transactionList ->
                Pair(watchedCoinList, transactionList)
            }).observeOn(schedulerProvider.ui())
                    .subscribe({
                        currentView?.onWatchedCoinsAndTransactionsLoaded(it.first, it.second)
                    }, {
                        Timber.e(it.localizedMessage)
                    })
            )
        }
    }

    override fun loadCoinsPrices(fromCurrencySymbol: String, toCurrencySymbol: String) {
        compositeDisposable.add(dashboardRepository.getCoinPriceFull(fromCurrencySymbol, toCurrencySymbol)
                .map { coinPriceList ->
                    val coinPriceMap: HashMap<String, CoinPrice> = hashMapOf()
                    coinPriceList.forEach { coinPrice ->
                        coinPrice.fromSymbol?.let { fromCurrencySymbol -> coinPriceMap.put(fromCurrencySymbol.toUpperCase(), coinPrice) }
                    }
                    if (coinPriceMap.isNotEmpty()) {
                        CoinyCache.coinPriceMap.putAll(coinPriceMap)
                    }
                    coinPriceMap
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({ currentView?.onCoinPricesLoaded(it) }, { Timber.e(it.localizedMessage) }))
    }

    override fun getTopCoinsByTotalVolume24hours(toCurrencySymbol: String) {
        compositeDisposable.add(coinRepo.getTopCoinsByTotalVolume24hours(toCurrencySymbol)
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    currentView?.onTopCoinsByTotalVolumeLoaded(it)
                    Timber.d("All Exchange Loaded")
                }, {
                    Timber.e(it.localizedMessage)
                })
        )
    }

    override fun getLatestNewsFromCryptoCompare() {
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
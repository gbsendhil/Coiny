package com.binarybricks.coiny.stories.settings

import SettingsContract
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.getCoinFromCCCoin
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import com.binarybricks.coiny.stories.CryptoCompareRepository
import com.binarybricks.coiny.utils.defaultExchange
import timber.log.Timber

/**
Created by Pranay Airan
 */

class SettingsPresenter(
        private val schedulerProvider: BaseSchedulerProvider,
        private val coinRepo: CryptoCompareRepository
) : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {

    override fun refreshCoinList(defaultCurrency: String) {
        compositeDisposable.add(coinRepo.getAllCoinsFromAPI()
                .map {
                    val coinList: MutableList<WatchedCoin> = mutableListOf()
                    val ccCoinList = it.first
                    ccCoinList.forEach { ccCoin ->
                        val coinInfo = it.second[ccCoin.symbol.toLowerCase()]
                        coinList.add(getCoinFromCCCoin(ccCoin, defaultExchange, defaultCurrency, coinInfo))
                    }
                    coinList
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("Inserted all coins in db with size ${it.size}")
                    currentView?.onCoinListRefreshed()
                }, {
                    currentView?.onNetworkError(it.localizedMessage)
                    Timber.e(it.localizedMessage)
                })
        )
    }

    override fun refreshExchangeList() {
        compositeDisposable.add(coinRepo.getExchangeInfo()
                .flatMap {
                    coinRepo.insertExchangeIntoList(it)
                }
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    Timber.d("all exchanges loaded and inserted into db")
                    currentView?.onExchangeListRefreshed()
                }, {
                    Timber.e(it.localizedMessage)
                    currentView?.onNetworkError(it.localizedMessage)
                }))
    }
}
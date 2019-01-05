package com.binarybricks.coiny.components.cointickermodule

import CoinTickerContract
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import timber.log.Timber

/**
 * Created by Pranay Airan
 */

class CoinTickerPresenter(private val schedulerProvider: BaseSchedulerProvider,
                          private val coinTickerRepository: CoinTickerRepository)
    : BasePresenter<CoinTickerContract.View>(), CoinTickerContract.Presenter {


    /**
     * Load the crypto ticker from the crypto panic api
     */
    override fun getCryptoTickers(coinName: String) {

        currentView?.showOrHideLoadingIndicator(true)

        compositeDisposable.add(coinTickerRepository.getCryptoTickers(coinName)
                .filter { it.isNotEmpty() }
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate { currentView?.showOrHideLoadingIndicator(false) }
                .subscribe({ currentView?.onPriceTickersLoaded(it) }, {
                    Timber.e(it.localizedMessage)
                    currentView?.onNetworkError("Error loading ticker")
                }))
    }
}
package com.binarybricks.coiny.components.cryptonewsmodule

import CryptoNewsContract
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.stories.BasePresenter
import timber.log.Timber

/**
 * Created by Pragya Agrawal
 */

class CryptoNewsPresenter(private val schedulerProvider: BaseSchedulerProvider, private val cryptoNewsRepository: CryptoNewsRepository)
    : BasePresenter<CryptoNewsContract.View>(), CryptoNewsContract.Presenter {

    /**
     * Load the crypto news from the crypto panic api
     */
    override fun getCryptoNews(coinSymbol: String) {

        currentView?.showOrHideLoadingIndicator(true)

        compositeDisposable.add(cryptoNewsRepository.getCryptoPanicNews(coinSymbol)
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate { currentView?.showOrHideLoadingIndicator(false) }
                .subscribe({ currentView?.onNewsLoaded(it) }, { Timber.e(it.localizedMessage) }))
    }
}
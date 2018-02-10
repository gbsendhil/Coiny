package com.binarybricks.coinhood.components.cryptonewsmodule

import CryptoNewsContract
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.BasePresenter
import timber.log.Timber

/**
 * Created by Pragya Agrawal
 */

class CryptoNewsPresenter(private val schedulerProvider: BaseSchedulerProvider) : BasePresenter<CryptoNewsContract.View>(), CryptoNewsContract.Presenter {

    private val cryptoNewsRepository by lazy {
        CryptoNewsRepository(schedulerProvider)
    }

    /**
     * Load the crypto news from the crypto panic api
     */
    override fun getCryptoNews(coinSymbol: String) {

        currentView?.showOrHideLoadingIndicator(true)

        compositeDisposable.add(cryptoNewsRepository.getCryptoPanicNews(coinSymbol)
                .filter { it.results?.isNotEmpty() == true }
                .observeOn(schedulerProvider.ui())
                .doAfterTerminate({ currentView?.showOrHideLoadingIndicator(false) })
                .subscribe({ currentView?.onNewsLoaded(it) }, { Timber.e(it.localizedMessage) }))
    }
}
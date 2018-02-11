package com.binarybricks.coinhood.stories.coindetails

import CoinDetailsContract
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.components.AboutCoinModule
import com.binarybricks.coinhood.components.AddCoinModule
import com.binarybricks.coinhood.components.CoinInfoModule
import com.binarybricks.coinhood.components.CoinStatsticsModule
import com.binarybricks.coinhood.components.cryptonewsmodule.CoinNewsModule
import com.binarybricks.coinhood.components.historicalchartmodule.CoinDetailsPresenter
import com.binarybricks.coinhood.components.historicalchartmodule.HistoricalChartModule
import com.binarybricks.coinhood.data.PreferenceHelper
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProvider
import com.binarybricks.coinhood.utils.ResourceProviderImpl
import com.binarybricks.coinhood.utils.defaultExchange
import com.binarybricks.coinhood.utils.getAboutStringForCoin
import kotlinx.android.synthetic.main.fragment_coin_details.*
import kotlinx.android.synthetic.main.fragment_coin_details.view.*


class CoinDetailsFragment : Fragment(), CoinDetailsContract.View {

    private val coinDetailList: MutableList<Any> = ArrayList()
    private var coinDetailsAdapter: CoinDetailsAdapter? = null

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }
    private val coinDetailsPresenter: CoinDetailsPresenter by lazy {
        CoinDetailsPresenter(schedulerProvider)
    }

    val resourceProvider: ResourceProvider by lazy {
        ResourceProviderImpl(activity)
    }
    val toCurrency: String by lazy {
        PreferenceHelper.getDefaultCurrency(context?.applicationContext)
    }

    companion object {
        private const val WATCHED_COIN = "WATCHED_COIN"
        @JvmStatic
        fun getArgumentBundle(watchedCoin: WatchedCoin): Bundle {
            val bundle = Bundle()
            bundle.putParcelable(WATCHED_COIN, watchedCoin)
            return bundle
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_coin_details, container, false)

        val watchedCoin = arguments?.getParcelable<WatchedCoin>(WATCHED_COIN)

        if (watchedCoin != null) {

            coinDetailsPresenter.attachView(this)

            lifecycle.addObserver(coinDetailsPresenter)

            inflate.rvCoinDetails.layoutManager = LinearLayoutManager(context)
            inflate.rvCoinDetails.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            showOrHideLoadingIndicator(true)

            // load data
            coinDetailsPresenter.loadCurrentCoinPrice(watchedCoin, toCurrency)
        }

        return inflate
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(rvCoinDetails, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {

    }

    override fun onCoinPriceLoaded(coinPrice: CoinPrice?, watchedCoin: WatchedCoin) {
        coinDetailList.add(HistoricalChartModule.HistoricalChartModuleData(coinPrice))

        coinDetailList.add(AddCoinModule.AddCoinModuleData())
        coinDetailList.add(CoinInfoModule.CoinInfoModuleData(coinPrice?.market
                ?: defaultExchange, watchedCoin.coin.algorithm, watchedCoin.coin.proofType))

        coinDetailList.add(CoinNewsModule.CoinNewsModuleData())
        if (coinPrice != null) {
            coinDetailList.add(CoinStatsticsModule.CoinStatsticsModuleData(coinPrice))
        }

        coinDetailList.add(AboutCoinModule.AboutCoinModuleData(getAboutStringForCoin(watchedCoin.coin.symbol, context?.applicationContext)))
        coinDetailsAdapter = CoinDetailsAdapter(watchedCoin.coin.symbol, toCurrency, watchedCoin.coin.fullName, lifecycle, coinDetailList, schedulerProvider, resourceProvider)

        view?.rvCoinDetails?.adapter = coinDetailsAdapter
        coinDetailsAdapter?.notifyDataSetChanged()
    }
}

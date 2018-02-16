package com.binarybricks.coiny.stories.coindetails

import CoinDetailsContract
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.AboutCoinModule
import com.binarybricks.coiny.components.AddCoinModule
import com.binarybricks.coiny.components.CoinInfoModule
import com.binarybricks.coiny.components.CoinStatsticsModule
import com.binarybricks.coiny.components.cryptonewsmodule.CoinNewsModule
import com.binarybricks.coiny.components.historicalchartmodule.CoinDetailsPresenter
import com.binarybricks.coiny.components.historicalchartmodule.HistoricalChartModule
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.utils.*
import kotlinx.android.synthetic.main.activity_all_coin_details.*
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

            val toolBarDefaultElevation = dpToPx(context, 12) // default elevation of toolbar

            inflate.rvCoinDetails.addOnScrollListener(object : OnVerticalScrollListener() {
                override fun onScrolled(offset: Int) {
                    super.onScrolled(offset)
                    (activity as CoinDetailsPagerActivity).toolbar?.elevation = Math.min(toolBarDefaultElevation.toFloat(), offset.toFloat())
                    (activity as CoinDetailsPagerActivity).toolBarTab?.elevation = Math.min(toolBarDefaultElevation.toFloat(), offset.toFloat())
                }
            })

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

        if (coinPrice != null) {
            coinDetailList.add(CoinStatsticsModule.CoinStatsticsModuleData(coinPrice))
        }

        coinDetailList.add(CoinInfoModule.CoinInfoModuleData(coinPrice?.market
                ?: defaultExchange, watchedCoin.coin.algorithm, watchedCoin.coin.proofType))

        coinDetailList.add(CoinNewsModule.CoinNewsModuleData())

        coinDetailList.add(AboutCoinModule.AboutCoinModuleData(getAboutStringForCoin(watchedCoin.coin.symbol, context?.applicationContext)))
        coinDetailsAdapter = CoinDetailsAdapter(watchedCoin.coin.symbol, toCurrency, watchedCoin.coin.fullName, lifecycle, coinDetailList, schedulerProvider, resourceProvider)

        view?.rvCoinDetails?.adapter = coinDetailsAdapter
        coinDetailsAdapter?.notifyDataSetChanged()
    }
}

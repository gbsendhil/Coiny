package com.binarybricks.coiny.stories.dashboard

import CoinDashboardContract
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.*
import com.binarybricks.coiny.components.historicalchartmodule.CoinDashboardPresenter
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.models.CryptoCompareNews
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.stories.CryptoCompareRepository
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.util.HashMap
import kotlin.collections.ArrayList

class CoinDashboardFragment : Fragment(), CoinDashboardContract.View {

    companion object {
        val TAG = "CoinDashboardFragment"
    }

    private var nextMenuItem: MenuItem? = null

    private var coinDashboardList: MutableList<ModuleItem> = ArrayList()
    private var coinDashboardAdapter: CoinDashboardAdapter? = null
    private var watchedCoinList: List<WatchedCoin> = emptyList()
    private var coinTransactionList: List<CoinTransaction> = emptyList()

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }

    private val dashboardRepository by lazy {
        DashboardRepository(schedulerProvider, CoinyApplication.database)
    }

    private val coinRepo by lazy {
        CryptoCompareRepository(schedulerProvider)
    }

    private val coinDashboardPresenter: CoinDashboardPresenter by lazy {
        CoinDashboardPresenter(schedulerProvider, dashboardRepository, coinRepo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val inflate = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val toolbar = inflate.toolbar
        toolbar?.title = "Market"

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        coinDashboardPresenter.attachView(this)

        lifecycle.addObserver(coinDashboardPresenter)

        // empty existing list
        coinDashboardList = ArrayList()
        coinDashboardList.add(0, CarousalModule.CarousalModuleData(null))
        coinDashboardList.add(1, DashboardNewsModule.DashboardNewsModuleData(null))

        initializeUI(inflate)

        // get top coins
        coinDashboardPresenter.getTopCoinsByTotalVolume24hours(PreferenceHelper.getDefaultCurrency(context))

        // get news
        coinDashboardPresenter.getLatestNewsFromCryptoCompare()

        // get prices for watched coin
        coinDashboardPresenter.loadWatchedCoinsAndTransactions()

        return inflate
    }

    private fun initializeUI(inflatedView: View) {

        inflatedView.rvDashboard.layoutManager = LinearLayoutManager(context)

        coinDashboardAdapter = CoinDashboardAdapter(PreferenceHelper.getDefaultCurrency(context), coinDashboardList, inflatedView.toolbarTitle)
        inflatedView.rvDashboard.adapter = coinDashboardAdapter
    }

    override fun onWatchedCoinsAndTransactionsLoaded(watchedCoinList: List<WatchedCoin>, coinTransactionList: List<CoinTransaction>) {

        this.watchedCoinList = watchedCoinList
        this.coinTransactionList = coinTransactionList

        getAllWatchedCoinsPrice()

        setupDashBoardAdapter(watchedCoinList, coinTransactionList)
    }

    private fun setupDashBoardAdapter(watchedCoinList: List<WatchedCoin>, coinTransactionList: List<CoinTransaction>) {

        // Add Dashboard Header with empty data
        // coinDashboardList.add(DashboardHeaderModule.DashboardHeaderModuleData(watchedCoinList, coinTransactionList, hashMapOf()))

        watchedCoinList.forEach { watchedCoin ->
            coinDashboardList.add(DashboardCoinModule.DashboardCoinModuleData(watchedCoin, null, coinTransactionList))
        }

        coinDashboardList.add(DashboardAddNewCoinModule.DashboardAddNewCoinModuleData())
        coinDashboardList.add(GenericFooterModule.FooterModuleData(getString(R.string.crypto_compare), getString(R.string.crypto_compare_url)))

        showOrHideLoadingIndicator(false)

        coinDashboardAdapter?.coinDashboardList = coinDashboardList
        coinDashboardAdapter?.notifyDataSetChanged()
    }

    private fun getAllWatchedCoinsPrice() {
        // we have all the watched coins now get price for all the coins
        var fromSymbol = ""
        watchedCoinList.forEachIndexed { index, watchedCoin ->
            if (index != watchedCoinList.size - 1) {
                fromSymbol = fromSymbol + watchedCoin.coin.symbol + ","
            } else {
                fromSymbol += watchedCoin.coin.symbol
            }
        }
        coinDashboardPresenter.loadCoinsPrices(fromSymbol, PreferenceHelper.getDefaultCurrency(context))
    }

    override fun onTopCoinsByTotalVolumeLoaded(topCoins: List<CoinPrice>) {

        val topCardList = mutableListOf<TopCardModule.TopCardsModuleData>()
        topCoins.forEach {
            topCardList.add(TopCardModule.TopCardsModuleData("${it.fromSymbol}/${it.toSymbol}", it.price
                    ?: "0", it.changePercentage24Hour ?: "0", it.marketCap ?: "0",
                    it.fromSymbol ?: ""))
        }

        coinDashboardAdapter?.let {
            if (!it.coinDashboardList.isNullOrEmpty()) {
                it.coinDashboardList[0] = CarousalModule.CarousalModuleData(topCardList)
                coinDashboardAdapter?.notifyItemChanged(0)
            }
        }
    }

    override fun onCoinNewsLoaded(coinNews: List<CryptoCompareNews>) {

        coinDashboardAdapter?.let {
            if (!it.coinDashboardList.isNullOrEmpty() && it.coinDashboardList.size >= 1) {
                it.coinDashboardList[1] = DashboardNewsModule.DashboardNewsModuleData(coinNews)
                coinDashboardAdapter?.notifyItemChanged(1)
            }
        }
    }

    override fun onCoinPricesLoaded(coinPriceListMap: HashMap<String, CoinPrice>) {

        val adapterDashboardList = coinDashboardAdapter?.coinDashboardList

        adapterDashboardList?.forEachIndexed { index, item ->
            if (item is DashboardCoinModule.DashboardCoinModuleData && coinPriceListMap.contains(item.watchedCoin.coin.symbol.toUpperCase())) {
                item.coinPrice = coinPriceListMap[item.watchedCoin.coin.symbol.toUpperCase()]
                coinDashboardAdapter?.notifyItemChanged(index)
            } else if (item is DashboardHeaderModule.DashboardHeaderModuleData) {
                item.coinPriceListMap = coinPriceListMap
                coinDashboardAdapter?.notifyItemChanged(index)
            }
        }

        // update dashboard card
    }

    override fun onNetworkError(errorMessage: String) {
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {
        if (!showLoading) {
            loadingAnimation.cancelAnimation()
            loadingAnimation.visibility = View.GONE
        } else {
            loadingAnimation.visibility = View.VISIBLE
            loadingAnimation.playAnimation()
        }
    }
}

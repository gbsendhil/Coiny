package com.binarybricks.coiny.stories.dashboard

import CoinDashboardContract
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.DashboardCoinModule
import com.binarybricks.coiny.components.DashboardHeaderModule
import com.binarybricks.coiny.components.GenericFooterModule
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.components.historicalchartmodule.CoinDashboardPresenter
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.stories.CryptoCompareRepository
import com.binarybricks.coiny.stories.coinsearch.CoinSearchActivity
import com.binarybricks.coiny.utils.OnVerticalScrollListener
import com.binarybricks.coiny.utils.dpToPx
import kotlinx.android.synthetic.main.activity_dashboard.loadingAnimation
import kotlinx.android.synthetic.main.activity_dashboard.view.rvDashboard
import kotlinx.android.synthetic.main.activity_dashboard.view.toolbar
import kotlinx.android.synthetic.main.activity_dashboard.view.toolbarTitle
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

        val inflate = inflater.inflate(R.layout.activity_dashboard, container, false)

        val toolbar = inflate.toolbar
        toolbar?.title = ""

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        initializeUI(inflate)

        coinDashboardPresenter.attachView(this)

        lifecycle.addObserver(coinDashboardPresenter)

        coinDashboardPresenter.loadWatchedCoinsAndTransactions()

        // get list of all exchanges
        coinDashboardPresenter.getAllSupportedExchanges()

        return inflate
    }

    private fun initializeUI(inflatedView: View) {
        val toolBarDefaultElevation = dpToPx(context, 8) // default elevation of toolbar

        inflatedView.rvDashboard.layoutManager = LinearLayoutManager(context)

        coinDashboardAdapter = CoinDashboardAdapter(PreferenceHelper.getDefaultCurrency(context), coinDashboardList, inflatedView.toolbarTitle)
        inflatedView.rvDashboard.adapter = coinDashboardAdapter
        inflatedView.rvDashboard.addOnScrollListener(object : OnVerticalScrollListener() {
            override fun onScrolled(offset: Int) {
                super.onScrolled(offset)
                inflatedView.toolbar.elevation = Math.min(toolBarDefaultElevation.toFloat(), offset.toFloat())
                inflatedView.toolbarTitle.alpha = Math.min(1.0f, offset / 60f) // approx height of header module
            }
        })
    }

    override fun onWatchedCoinsAndTransactionsLoaded(watchedCoinList: List<WatchedCoin>, coinTransactionList: List<CoinTransaction>) {

        this.watchedCoinList = watchedCoinList
        this.coinTransactionList = coinTransactionList

        getAllWatchedCoinsPrice()

        setupDashBoardAdapter(watchedCoinList, coinTransactionList)
    }

    private fun setupDashBoardAdapter(watchedCoinList: List<WatchedCoin>, coinTransactionList: List<CoinTransaction>) {

        // empty existing list
        coinDashboardList = ArrayList()

        // Add Dashboard Header with empty data
        coinDashboardList.add(DashboardHeaderModule.DashboardHeaderModuleData(watchedCoinList, coinTransactionList, hashMapOf()))

        watchedCoinList.forEach { watchedCoin ->
            coinDashboardList.add(DashboardCoinModule.DashboardCoinModuleData(watchedCoin, null, coinTransactionList))
        }

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.home_menu, menu)

        nextMenuItem = menu?.findItem(R.id.action_search)

        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                context?.let {
                    startActivity(CoinSearchActivity.buildLaunchIntent(it))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

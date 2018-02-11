package com.binarybricks.coinhood.stories.dashboard

import CoinDashboardContract
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.binarybricks.coinhood.CoinHoodApplication
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.components.DashboardCoinListHeaderModule
import com.binarybricks.coinhood.components.DashboardCoinModule
import com.binarybricks.coinhood.components.DashboardEmptyCardModule
import com.binarybricks.coinhood.components.DashboardHeaderModule
import com.binarybricks.coinhood.components.historicalchartmodule.CoinDashboardPresenter
import com.binarybricks.coinhood.data.PreferenceHelper
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.utils.OnVerticalScrollListener
import com.binarybricks.coinhood.utils.dpToPx
import com.lapism.searchview.SearchAdapter
import com.lapism.searchview.SearchItem
import com.lapism.searchview.SearchView
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.HashMap
import kotlin.collections.ArrayList


class CoinDashboardActivity : AppCompatActivity(), CoinDashboardContract.View {

    companion object {
        @JvmStatic
        fun buildLaunchIntent(context: Context): Intent {
            return Intent(context, CoinDashboardActivity::class.java)
        }
    }

    private var nextMenuItem: MenuItem? = null

    private var coinDashboardList: MutableList<Any> = ArrayList()
    private var coinDashboardAdapter: CoinDashboardAdapter? = null
    private var watchedCoinList: List<WatchedCoin> = emptyList()

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }
    private val coinDashboardPresenter: CoinDashboardPresenter by lazy {
        CoinDashboardPresenter(schedulerProvider, CoinHoodApplication.database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.title = ""


        val toolBarDefaultElevation = dpToPx(this, 8) // default elevation of toolbar

        setupSearchView()

        coinDashboardPresenter.attachView(this)
        lifecycle.addObserver(coinDashboardPresenter)

        coinDashboardPresenter.loadWatchedCoins()

        rvDashboard.layoutManager = LinearLayoutManager(this)

        coinDashboardAdapter = CoinDashboardAdapter(PreferenceHelper.getDefaultCurrency(this), coinDashboardList)
        rvDashboard.adapter = coinDashboardAdapter

        rvDashboard.addOnScrollListener(object : OnVerticalScrollListener() {
            override fun onScrolled(offset: Int) {
                super.onScrolled(offset)
                toolbar.elevation = Math.min(toolBarDefaultElevation.toFloat(), offset.toFloat())
                toolbarTitle.alpha = Math.min(1.0f, offset / 60f) // approx height of header module
            }
        })
    }

    // Menu icons are inflated just as they were with actionbar
    override fun onCreateOptionsMenu(
        menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)

        nextMenuItem = menu.findItem(R.id.action_search)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_search -> {
                searchView.open(true) // enable or disable animation
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

    override fun onWatchedCoinsLoaded(watchedCoinList: List<WatchedCoin>?) {
        if (watchedCoinList != null) {
            this.watchedCoinList = watchedCoinList
            // we have all the watched coins now get price for all the coins
            var fromSymbol = ""
            watchedCoinList.forEachIndexed { index, watchedCoin ->
                if (index != watchedCoinList.size - 1) {
                    fromSymbol = fromSymbol + watchedCoin.coin.symbol + ","
                } else {
                    fromSymbol += watchedCoin.coin.symbol
                }
            }
            coinDashboardPresenter.loadCoinsPrices(fromSymbol, PreferenceHelper.getDefaultCurrency(this))
        }

    }

    override fun onCoinPricesLoaded(coinPriceListMap: HashMap<String, CoinPrice>) {

        // empty existing list
        coinDashboardList = ArrayList()

        // pass this text view in future to dashboard header
        toolbarTitle.text = "$10.00"

        coinDashboardList.add(DashboardHeaderModule.DashboardHeaderModuleData())

        val coinWatchList: MutableList<Any> = ArrayList()
        coinWatchList.add(DashboardCoinListHeaderModule.DashboardCoinListHeaderModuleData("Watchlist"))

        val coinPurchasesList: MutableList<Any> = ArrayList()
        coinPurchasesList.add(DashboardCoinListHeaderModule.DashboardCoinListHeaderModuleData("Crypto Currencies"))

        watchedCoinList.forEach { watchedCoin ->
            if (watchedCoin.purchased && coinPriceListMap.contains(watchedCoin.coin.symbol.toUpperCase())) {
                coinPriceListMap[watchedCoin.coin.symbol.toUpperCase()]?.let {
                    coinPurchasesList.add(DashboardCoinModule.DashboardCoinModuleData(watchedCoin, it))
                }
            } else if (coinPriceListMap.contains(watchedCoin.coin.symbol.toUpperCase())) {
                coinPriceListMap[watchedCoin.coin.symbol.toUpperCase()]?.let {
                    coinWatchList.add(DashboardCoinModule.DashboardCoinModuleData(watchedCoin, it))
                }
            }
        }

        if (coinPurchasesList.size == 1) {
            coinPurchasesList.add(DashboardEmptyCardModule.DashboardEmptyCardModuleData("Track & monitor your holding. Add your first Transaction"))
        }

        if (coinWatchList.size == 1) {
            coinWatchList.add(DashboardEmptyCardModule.DashboardEmptyCardModuleData("Watch coin prices. Search and click + to get started."))
        }

        coinDashboardList.addAll(coinPurchasesList)
        coinDashboardList.addAll(coinWatchList)

        showOrHideLoadingIndicator(false)

        coinDashboardAdapter?.coinDashboardList = coinDashboardList
        coinDashboardAdapter?.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        searchView.version = SearchView.Version.MENU_ITEM
        searchView.versionMargins = SearchView.VersionMargins.MENU_ITEM
        searchView.theme = SearchView.Theme.DARK

        searchView.hint = "Search Coin"
        searchView.setVoice(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setNavigationIconClickListener({
            searchView.close(true)
        })

        searchView.setOnOpenCloseListener(object : SearchView.OnOpenCloseListener {
            override fun onOpen(): Boolean {
                nextMenuItem?.isVisible = false
                return true
            }

            override fun onClose(): Boolean {
                nextMenuItem?.isVisible = true
                return true
            }
        })

        val suggestionsList = ArrayList<SearchItem>()
        suggestionsList.add(SearchItem("Bitcoin"))
        suggestionsList.add(SearchItem("Bitcoin Cash"))
        suggestionsList.add(SearchItem("Litcoin"))

        val searchAdapter = SearchAdapter(this, suggestionsList)
        searchAdapter.setOnSearchItemClickListener { view, position, text -> searchView.close(false) }
        searchView.adapter = searchAdapter
    }
}

package com.binarybricks.coinhood.stories.coindetails

import CoinDetailContract
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.components.AboutCoinModule
import com.binarybricks.coinhood.components.AddCoinModule
import com.binarybricks.coinhood.components.CoinInfoModule
import com.binarybricks.coinhood.components.CoinStatsticsModule
import com.binarybricks.coinhood.components.cryptonewsmodule.CoinNewsModule
import com.binarybricks.coinhood.components.historicalchartmodule.CoinDetailPresenter
import com.binarybricks.coinhood.components.historicalchartmodule.HistoricalChartModule
import com.binarybricks.coinhood.data.PreferenceHelper
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProviderImpl
import com.binarybricks.coinhood.utils.defaultExchange
import com.binarybricks.coinhood.utils.getAboutStringForCoin
import kotlinx.android.synthetic.main.activity_coin_details.*


class CoinDetailsActivity : AppCompatActivity(), CoinDetailContract.View {

    private val coinDetailList: MutableList<Any> = ArrayList()
    private var coinDetailsAdapter: CoinDetailsAdapter? = null

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }
    private val coinDetailPresenter: CoinDetailPresenter by lazy {
        CoinDetailPresenter(schedulerProvider)
    }

    companion object {
        private const val WATCHED_COIN = "WATCHED_COIN"
        private const val COIN_PRICE = "COIN_PRICE"

        @JvmStatic
        fun buildLaunchIntent(context: Context, watchedCoin: WatchedCoin, coinPrice: CoinPrice): Intent {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra(WATCHED_COIN, watchedCoin)
            intent.putExtra(COIN_PRICE, coinPrice)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val resourceProvider = ResourceProviderImpl(applicationContext)

        val watchedCoin = intent.getParcelableExtra<WatchedCoin>(WATCHED_COIN)
        val coinPrice = intent.getParcelableExtra<CoinPrice>(COIN_PRICE)


        val toCurrency = PreferenceHelper.getDefaultCurrency(this)

        supportActionBar?.title = "${watchedCoin.coin.fullName.trim()}"

        coinDetailPresenter.attachView(this)

        lifecycle.addObserver(coinDetailPresenter)

        rvCoinDetails.layoutManager = LinearLayoutManager(this)
        rvCoinDetails.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        coinDetailList.add(AddCoinModule.AddCoinModuleData())
        coinDetailList.add(CoinInfoModule.CoinInfoModuleData(coinPrice.market
                ?: defaultExchange, watchedCoin.coin.algorithm, watchedCoin.coin.proofType))

        coinDetailList.add(CoinNewsModule.CoinNewsModuleData())
        coinDetailList.add(CoinStatsticsModule.CoinStatsticsModuleData(coinPrice))
        coinDetailList.add(AboutCoinModule.AboutCoinModuleData(getAboutStringForCoin(watchedCoin.coin.symbol, applicationContext)))

        coinDetailsAdapter = CoinDetailsAdapter(watchedCoin.coin.symbol, toCurrency, watchedCoin.coin.fullName, lifecycle, coinDetailList, schedulerProvider, resourceProvider)
        rvCoinDetails.adapter = coinDetailsAdapter

        // load data
        onCoinDataLoaded(coinPrice)
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(rvCoinDetails, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {

    }

    override fun onCoinDataLoaded(coinPrice: CoinPrice?) {
        coinDetailList.add(0, HistoricalChartModule.HistoricalChartModuleData(coinPrice))
        coinDetailsAdapter?.notifyDataSetChanged()
    }
}

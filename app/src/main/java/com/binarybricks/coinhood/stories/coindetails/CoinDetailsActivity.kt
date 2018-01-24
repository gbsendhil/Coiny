package com.binarybricks.coinhood.stories.coindetails

import CoinDetailContract
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.components.AboutCoinModule
import com.binarybricks.coinhood.components.historicalchart.CoinDetailPresenter
import com.binarybricks.coinhood.components.historicalchart.HistoricalChartModule
import com.binarybricks.coinhood.network.models.Coin
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProviderImpl
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val resourceProvider = ResourceProviderImpl(applicationContext)

        val fromCoin = intent.getStringExtra(FROM_COIN)

        // precondition to make sure this code is not executed
        requireNotNull(fromCoin)

        val toCurrency = "USD"

        supportActionBar?.title = fromCoin

        coinDetailPresenter.attachView(this)

        lifecycle.addObserver(coinDetailPresenter)

        rvCoinDetails.layoutManager = LinearLayoutManager(this)
        rvCoinDetails.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        coinDetailList.add(AboutCoinModule.AboutCoinModuleData(getAboutStringForCoin(fromCoin, applicationContext)))

        coinDetailsAdapter = CoinDetailsAdapter(fromCoin, toCurrency, lifecycle, coinDetailList, schedulerProvider, resourceProvider)
        rvCoinDetails.adapter = coinDetailsAdapter

        // load data
        coinDetailPresenter.loadCurrentCoinPrice(fromCoin, toCurrency)
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(rvCoinDetails, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {

    }

    override fun onCoinDataLoaded(coin: Coin?) {
        coinDetailList.add(0, HistoricalChartModule.HistoricalChartModuleData(coin))
        coinDetailsAdapter?.notifyDataSetChanged()
    }

    companion object {
        private const val FROM_COIN = "FROM_COIN"

        @JvmStatic
        fun buildLaunchIntent(context: Context, fromCoinSymbol: String): Intent {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra(FROM_COIN, fromCoinSymbol.toUpperCase())
            return intent
        }
    }
}

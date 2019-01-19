package com.binarybricks.coiny.stories.ticker

import CoinTickerContract
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.cointickermodule.CoinTickerPresenter
import com.binarybricks.coiny.components.cointickermodule.CoinTickerRepository
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.network.models.CryptoTicker
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.utils.ResourceProviderImpl
import com.binarybricks.coiny.utils.openCustomTab
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_coin_ticker_list.*
import java.util.*

/**
 * Created by Pranay Airan
 * Activity showing all ticker data
 */
class CoinTickerActivity : AppCompatActivity(), CoinTickerContract.View {

    companion object {
        private const val COIN_NAME = "COIN_FULL_NAME"

        @JvmStatic
        fun buildLaunchIntent(context: Context, coinName: String): Intent {
            val intent = Intent(context, CoinTickerActivity::class.java)
            intent.putExtra(COIN_NAME, coinName)
            return intent
        }
    }

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.instance
    }

    private val coinTickerRepository by lazy {
        CoinTickerRepository(schedulerProvider, CoinyApplication.database)
    }

    private val resourceProvider by lazy {
        ResourceProviderImpl(this)
    }

    private val coinTickerPresenter: CoinTickerPresenter by lazy {
        CoinTickerPresenter(schedulerProvider, coinTickerRepository, resourceProvider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_ticker_list)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val coinName = intent.getStringExtra(COIN_NAME).trim()

        supportActionBar?.title = getString(R.string.tickerActivityTitle, coinName)

        rvCoinTickerList.layoutManager = LinearLayoutManager(this)

        coinTickerPresenter.attachView(this)

        lifecycle.addObserver(coinTickerPresenter)

        coinTickerPresenter.getCryptoTickers(coinName.toLowerCase())

        Crashlytics.log("CoinTickerActivity")
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {
        if (!showLoading) {
            pbLoading.hide()
        } else {
            pbLoading.show()
        }
    }

    override fun onPriceTickersLoaded(tickerData: List<CryptoTicker>) {
        val coinTickerAdapter = CoinTickerAdapter(tickerData,
                Currency.getInstance(PreferenceHelper.getDefaultCurrency(this)), resourceProvider)

        rvCoinTickerList.adapter = coinTickerAdapter

        tvFooter.setOnClickListener {
            openCustomTab(getString(R.string.coin_gecko_url), this)
        }
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(rvCoinTickerList, errorMessage, Snackbar.LENGTH_LONG)
    }
}

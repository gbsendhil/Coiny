package com.binarybricks.coiny.stories.coindetails

import CoinDetailsContract
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.stories.CryptoCompareRepository
import com.binarybricks.coiny.stories.coin.CoinFragment
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_coin_details.*

class CoinDetailsActivity : AppCompatActivity(), CoinDetailsContract.View {

    companion object {
        private const val WATCHED_COIN = "WATCHED_COIN"
        private const val COIN_SYMBOL = "COIN_SYMBOL"

        @JvmStatic
        fun buildLaunchIntent(context: Context, watchedCoin: WatchedCoin): Intent {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra(WATCHED_COIN, watchedCoin)
            return intent
        }

        @JvmStatic
        fun buildLaunchIntent(context: Context, symbol: String): Intent {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra(COIN_SYMBOL, symbol)
            return intent
        }
    }

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.instance
    }

    private val coinRepo by lazy {
        CryptoCompareRepository(schedulerProvider, CoinyApplication.database)
    }

    private val coinDetailPresenter: CoinDetailPresenter by lazy {
        CoinDetailPresenter(schedulerProvider, coinRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.elevation = 0f

        coinDetailPresenter.attachView(this)

        lifecycle.addObserver(coinDetailPresenter)

        val watchedCoin: WatchedCoin? = intent.getParcelableExtra(WATCHED_COIN)

        if (watchedCoin != null) {
            onWatchedCoinLoaded(watchedCoin)
        } else {
            coinDetailPresenter.getWatchedCoinFromSymbol(intent.getStringExtra(COIN_SYMBOL))
        }

        Crashlytics.log("CoinDetailsActivity")
    }

    override fun onWatchedCoinLoaded(coin: WatchedCoin?) {
        if (coin != null) {
            showOrHideLoadingIndicator(false)

            val coinDetailsFragment = CoinFragment()
            coinDetailsFragment.arguments = CoinFragment.getArgumentBundle(coin)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flCoinDetails, coinDetailsFragment)
            fragmentTransaction.commit()

            supportActionBar?.title = getString(R.string.transactionTypeWithQuantity,
                    coin.coin.coinName, coin.coin.symbol)
        }
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {
        if (!showLoading) {
            pbLoading.hide()
        } else {
            pbLoading.show()
        }
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(flCoinDetails, errorMessage, Snackbar.LENGTH_LONG)
    }
}

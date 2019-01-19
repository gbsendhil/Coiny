package com.binarybricks.coiny.stories.coindetails

import CoinDetailsPagerContract
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_pager_coin_details.*
import kotlinx.android.synthetic.main.fragment_coin_details.*

class CoinDetailsPagerActivity : AppCompatActivity(), CoinDetailsPagerContract.View {

    private var watchedCoin: WatchedCoin? = null
    var isCoinInfoChanged = false

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.instance
    }

    private val allCoinDetailsRepository by lazy {
        CoinDetailsPagerRepository(schedulerProvider, CoinyApplication.database)
    }

    private val coinDetailPagerPresenter: CoinDetailPagerPresenter by lazy {
        CoinDetailPagerPresenter(schedulerProvider, allCoinDetailsRepository)
    }

    companion object {
        private const val WATCHED_COIN = "WATCHED_COIN"

        @JvmStatic
        fun buildLaunchIntent(context: Context, watchedCoin: WatchedCoin): Intent {
            val intent = Intent(context, CoinDetailsPagerActivity::class.java)
            intent.putExtra(WATCHED_COIN, watchedCoin)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager_coin_details)

        toolbar.elevation = 0f

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        watchedCoin = intent.getParcelableExtra(WATCHED_COIN)

        coinDetailPagerPresenter.attachView(this)

        lifecycle.addObserver(coinDetailPagerPresenter)

        showOrHideLoadingIndicator(true)

        coinDetailPagerPresenter.loadWatchedCoins()

        Crashlytics.log("CoinDetailsPagerActivity")
    }

    override fun onWatchedCoinsLoaded(watchedCoinList: List<WatchedCoin>?) {

        supportActionBar?.title = getString(R.string.transactionTypeWithQuantity,
                watchedCoin?.coin?.coinName, watchedCoin?.coin?.symbol)

        val allCoinsPagerAdapter = CoinDetailsPagerAdapter(watchedCoinList, supportFragmentManager)
        vpCoins.adapter = allCoinsPagerAdapter

        showOrHideLoadingIndicator(false)

        watchedCoinList?.forEachIndexed { index, watch ->
            if (watchedCoin?.coin?.name == watch.coin.name) {
                vpCoins.currentItem = index
            }
        }

        vpCoins.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                supportActionBar?.title = watchedCoinList?.get(position)?.coin?.coinName
            }
        })
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(rvCoinDetails, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    private fun showOrHideLoadingIndicator(showLoading: Boolean) {
        if (showLoading) {
            pbLoading.visibility = View.VISIBLE
        } else {
            pbLoading.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isCoinInfoChanged) {
            setResult(Activity.RESULT_OK)
        }

        finish()
    }
}

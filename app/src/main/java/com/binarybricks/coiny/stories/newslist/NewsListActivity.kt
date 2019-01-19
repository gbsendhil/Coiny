package com.binarybricks.coiny.stories.newslist

import CryptoNewsContract
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.cryptonewsmodule.CryptoNewsPresenter
import com.binarybricks.coiny.components.cryptonewsmodule.CryptoNewsRepository
import com.binarybricks.coiny.network.models.CryptoPanicNews
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.utils.ResourceProvider
import com.binarybricks.coiny.utils.ResourceProviderImpl
import com.binarybricks.coiny.utils.openCustomTab
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_news_list.*

/**
 * Created by Pragya Agrawal
 * Activity showing all news items
 */
class NewsListActivity : AppCompatActivity(), CryptoNewsContract.View {

    companion object {
        private const val COIN_FULL_NAME = "COIN_FULL_NAME"
        private const val COIN_SYMBOL = "COIN_SYMBOL"

        @JvmStatic
        fun buildLaunchIntent(context: Context, coinName: String, coinSymbol: String): Intent {
            val intent = Intent(context, NewsListActivity::class.java)
            intent.putExtra(COIN_FULL_NAME, coinName)
            intent.putExtra(COIN_SYMBOL, coinSymbol)
            return intent
        }
    }

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.instance
    }

    private val cryptoNewsRepository by lazy {
        CryptoNewsRepository(schedulerProvider)
    }
    private val cryptoNewsPresenter: CryptoNewsPresenter by lazy {
        CryptoNewsPresenter(schedulerProvider, cryptoNewsRepository)
    }

    private val resourceProvider: ResourceProvider by lazy {
        ResourceProviderImpl(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val coinFullName = intent.getStringExtra(COIN_FULL_NAME).trim()
        val coinSymbol = intent.getStringExtra(COIN_SYMBOL).trim()

        supportActionBar?.title = getString(R.string.newsActivityTitle, coinFullName)

        rvNewsList.layoutManager = LinearLayoutManager(this)

        cryptoNewsPresenter.attachView(this)

        lifecycle.addObserver(cryptoNewsPresenter)

        cryptoNewsPresenter.getCryptoNews(coinSymbol)

        Crashlytics.log("NewsListActivity")
    }

    override fun showOrHideLoadingIndicator(showLoading: Boolean) {
        if (!showLoading) {
            pbLoading.hide()
        } else {
            pbLoading.show()
        }
    }

    override fun onNewsLoaded(cryptoPanicNews: CryptoPanicNews) {
        val newsListAdapter = NewsListAdapter(cryptoPanicNews, resourceProvider)
        rvNewsList.adapter = newsListAdapter

        tvFooter.setOnClickListener {
            openCustomTab(getString(R.string.crypto_panic_url), this)
        }
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(rvNewsList, errorMessage, Snackbar.LENGTH_LONG)
    }
}

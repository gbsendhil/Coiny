package com.binarybricks.coiny.stories.coindetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.WatchedCoin


class CoinDetailsActivity : AppCompatActivity() {

    private var watchedCoin: WatchedCoin? = null

    companion object {
        private const val WATCHED_COIN = "WATCHED_COIN"

        @JvmStatic
        fun buildLaunchIntent(context: Context, watchedCoin: WatchedCoin): Intent {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra(WATCHED_COIN, watchedCoin)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.elevation = 0f

        watchedCoin = intent.getParcelableExtra(WATCHED_COIN)
        watchedCoin?.let {
            val coinDetailsFragment = CoinDetailsFragment()
            coinDetailsFragment.arguments = CoinDetailsFragment.getArgumentBundle(it)

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flCoinDetails, coinDetailsFragment)
            fragmentTransaction.commit()

            supportActionBar?.title = "${it.coin.coinName}(${it.coin.symbol})"
        }
    }
}

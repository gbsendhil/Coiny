package com.binarybricks.coiny.stories.newslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coiny.R
import com.binarybricks.coiny.network.models.CryptoPanicNews
import kotlinx.android.synthetic.main.activity_news_list.*

/**
 * Created by Pragya Agrawal
 * Activity showing all news items
 */
class NewsListActivity : AppCompatActivity() {

    companion object {
        private const val COIN_FULL_NAME = "COIN_FULL_NAME"
        private const val COIN_NEWS = "COIN_NEWS"

        @JvmStatic
        fun buildLaunchIntent(context: Context, coinName: String, cryptoPanicNews: CryptoPanicNews): Intent {
            val intent = Intent(context, NewsListActivity::class.java)
            intent.putExtra(COIN_FULL_NAME, coinName)
            intent.putExtra(COIN_NEWS, cryptoPanicNews)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val coinFullName = intent.getStringExtra(COIN_FULL_NAME).trim()
        val cryptoPanicNews = intent.getParcelableExtra<CryptoPanicNews>(COIN_NEWS)

        supportActionBar?.title = "$coinFullName News"

        rvNewsList.layoutManager = LinearLayoutManager(this)

        val newsListAdapter = NewsListAdapter(cryptoPanicNews)
        rvNewsList.adapter = newsListAdapter
    }
}

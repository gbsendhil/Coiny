package com.binarybricks.coiny.stories.coin

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.binarybricks.coiny.adapterdelegates.*
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
Created by Pranay Airan 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinAdapter(
        fromCurrency: String,
        toCurrency: String,
        coinName: String,
        var coinDetailList: List<ModuleItem>,
        coinyDatabase: CoinyDatabase?,
        schedulerProvider: BaseSchedulerProvider,
        resourceProvider: ResourceProvider
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HISTORICAL_CHART = 0
        private const val ADD_COIN = 1
        private const val COIN_POSITION = 2
        private const val COIN_INFO = 3
        private const val COIN_NEWS = 4
        private const val COIN_STATS = 5
        private const val ABOUT_COIN = 6
        private const val COIN_TRANSACTION = 7
        private const val FOOTER = 8
        private const val COIN_TICKER = 9
    }

    private val delegates: AdapterDelegatesManager<List<ModuleItem>> = AdapterDelegatesManager()

    private val coinTickerAdapterDelegate by lazy {
        CoinTickerAdapterDelegate(coinName, schedulerProvider, coinyDatabase, resourceProvider)
    }

    private val historicalChartAdapterDelegate by lazy {
        HistoricalChartAdapterDelegate(fromCurrency, toCurrency, schedulerProvider, resourceProvider)
    }

    private val coinNewsAdapterDelegate by lazy {
        CoinNewsAdapterDelegate(fromCurrency, coinName, schedulerProvider, resourceProvider)
    }

    init {
        delegates.addDelegate(HISTORICAL_CHART, historicalChartAdapterDelegate)
        delegates.addDelegate(ADD_COIN, AddCoinAdapterDelegate())
        delegates.addDelegate(COIN_POSITION, CoinPositionAdapterDelegate(resourceProvider))
        delegates.addDelegate(COIN_INFO, CoinInfoAdapterDelegate())
        delegates.addDelegate(COIN_NEWS, coinNewsAdapterDelegate)
        delegates.addDelegate(COIN_TICKER, coinTickerAdapterDelegate)
        delegates.addDelegate(COIN_STATS, CoinStatsAdapterDelegate(resourceProvider))
        delegates.addDelegate(ABOUT_COIN, AboutCoinAdapterDelegate())
        delegates.addDelegate(COIN_TRANSACTION, CoinTransactionAdapterDelegate(resourceProvider))
        delegates.addDelegate(FOOTER, GenericFooterAdapterDelegate())
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.getItemViewType(coinDetailList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        delegates.onBindViewHolder(coinDetailList, position, viewHolder)
    }

    override fun getItemCount(): Int {
        return coinDetailList.size
    }

    fun cleanup() {
        historicalChartAdapterDelegate.cleanup()
        coinTickerAdapterDelegate.cleanup()
        coinNewsAdapterDelegate.cleanup()
    }
}
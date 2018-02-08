package com.binarybricks.coinhood.stories.coindetails

import android.arch.lifecycle.Lifecycle
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.binarybricks.coinhood.adapterdelegates.AboutCoinAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.CoinPositionAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.CoinStatsAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.HistoricalChartAdapterDelegate
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager


/**
 * Created by pranay airan on 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinDetailsAdapter(fromCurrency: String,
                         toCurrency: String,
                         lifecycle: Lifecycle,
                         private val coinDetailList: List<Any>,
                         schedulerProvider: BaseSchedulerProvider,
                         resourceProvider: ResourceProvider) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HISTORICAL_CHART = 0
    private val COIN_STATS = 1
    private val COIN_POSITION = 2
    private val ABOUT_COIN = 3

    val delegates: AdapterDelegatesManager<List<Any>> = AdapterDelegatesManager()

    init {
        delegates.addDelegate(HISTORICAL_CHART, HistoricalChartAdapterDelegate(fromCurrency, toCurrency, schedulerProvider, lifecycle, resourceProvider))
        delegates.addDelegate(COIN_STATS, CoinStatsAdapterDelegate())
        delegates.addDelegate(COIN_POSITION, CoinPositionAdapterDelegate())
        delegates.addDelegate(ABOUT_COIN, AboutCoinAdapterDelegate())
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
}
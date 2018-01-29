package com.binarybricks.coinhood.adapterdelegates

import android.arch.lifecycle.Lifecycle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.historicalchart.HistoricalChartModule
import com.binarybricks.coinhood.network.models.Coin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by pranay airan on 1/23/18.
 */

class HistoricalChartAdapterDelegate(private val fromCurrency: String,
                                     private val toCurrency: String,
                                     private val schedulerProvider: BaseSchedulerProvider,
                                     private val lifecycle: Lifecycle,
                                     private val resourceProvider: ResourceProvider) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is HistoricalChartModule.HistoricalChartModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val historicalChartModule = HistoricalChartModule(schedulerProvider, resourceProvider, fromCurrency, toCurrency)
        lifecycle.addObserver(historicalChartModule)

        val historicalChartModuleView = historicalChartModule.init(parent.context, parent)
        return HistoricalChartViewHolder(historicalChartModuleView, historicalChartModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        // load data
        val historicalChartViewHolder = holder as HistoricalChartViewHolder
        val historicalChartModuleData = items[position] as HistoricalChartModule.HistoricalChartModuleData

        historicalChartViewHolder.loadHistoricalChartData()
        historicalChartViewHolder.addCoinAndAnimateCoinPrice(historicalChartModuleData.coinWithCurrentPrice)
    }

    class HistoricalChartViewHolder(itemView: View, private val historicalChartModule: HistoricalChartModule) : RecyclerView.ViewHolder(itemView) {
        fun loadHistoricalChartData() {
            historicalChartModule.loadData()
        }

        fun addCoinAndAnimateCoinPrice(coin: Coin?) {
            historicalChartModule.addCoinAndAnimateCoinPrice(coin)
        }
    }
}
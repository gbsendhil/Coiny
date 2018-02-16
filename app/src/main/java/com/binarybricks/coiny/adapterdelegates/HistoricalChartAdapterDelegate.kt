package com.binarybricks.coiny.adapterdelegates

import android.arch.lifecycle.Lifecycle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.historicalchartmodule.HistoricalChartModule
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pranay Airan
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

        val historicalChartModuleView = historicalChartModule.init(LayoutInflater.from(parent.context), parent)
        return HistoricalChartViewHolder(historicalChartModuleView, historicalChartModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        // load data
        val historicalChartViewHolder = holder as HistoricalChartViewHolder
        val historicalChartModuleData = items[position] as HistoricalChartModule.HistoricalChartModuleData

        historicalChartViewHolder.loadHistoricalChartData()
        historicalChartViewHolder.addCoinAndAnimateCoinPrice(historicalChartModuleData.coinPriceWithCurrentPrice)
    }

    class HistoricalChartViewHolder(itemView: View, private val historicalChartModule: HistoricalChartModule) : RecyclerView.ViewHolder(itemView) {
        fun loadHistoricalChartData() {
            historicalChartModule.loadData(itemView)
        }

        fun addCoinAndAnimateCoinPrice(coinPrice: CoinPrice?) {
            historicalChartModule.addCoinAndAnimateCoinPrice(coinPrice)
        }
    }
}
package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.components.cryptonewsmodule.CoinNewsModule
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by Pranay Airan
 */

class CoinNewsAdapterDelegate(
        private val coinSymbol: String,
        private val coinName: String,
        private val schedulerProvider: BaseSchedulerProvider,
        private val resourceProvider: ResourceProvider
) : AdapterDelegate<List<ModuleItem>>() {

    private val coinNewsModule by lazy {
        CoinNewsModule(schedulerProvider, coinSymbol, coinName, resourceProvider)
    }

    override fun isForViewType(items: List<ModuleItem>, position: Int): Boolean {
        return items[position] is CoinNewsModule.CoinNewsModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val coinNewsModuleView = coinNewsModule.init(LayoutInflater.from(parent.context), parent)
        return CoinNewsViewHolder(coinNewsModuleView, coinNewsModule)
    }

    override fun onBindViewHolder(items: List<ModuleItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        // load data
        val historicalChartViewHolder = holder as CoinNewsViewHolder
        historicalChartViewHolder.loadCoinNewsData()
    }

    fun cleanup() {
        coinNewsModule.cleanUp()
    }

    class CoinNewsViewHolder(override val containerView: View, private val coinNewsModule: CoinNewsModule)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun loadCoinNewsData() {
            coinNewsModule.loadNewsData(itemView)
        }
    }
}
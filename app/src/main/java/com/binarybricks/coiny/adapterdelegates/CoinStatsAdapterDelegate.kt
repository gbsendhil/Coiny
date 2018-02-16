package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.CoinStatsticsModule
import com.binarybricks.coiny.network.models.CoinPrice
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pragya Agrawal
 */

class CoinStatsAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is CoinStatsticsModule.CoinStatsticsModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val coinStatsModule = CoinStatsticsModule()
        val coinStatsModuleView = coinStatsModule.init(LayoutInflater.from(parent.context), parent)
        return CoinStatsViewHolder(coinStatsModuleView, coinStatsModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val aboutCoinViewHolder = holder as CoinStatsViewHolder
        aboutCoinViewHolder.showAboutCoinText((items[position] as CoinStatsticsModule.CoinStatsticsModuleData).coinPrice)
    }

    class CoinStatsViewHolder(itemView: View, private val coinStatisticsModule: CoinStatsticsModule) : RecyclerView.ViewHolder(itemView) {
        fun showAboutCoinText(coinPrice: CoinPrice) {
            coinStatisticsModule.showCoinStats(itemView, coinPrice)
        }
    }
}
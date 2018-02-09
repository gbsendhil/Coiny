package com.binarybricks.coinhood.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.CoinInfoModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * @author Pragya Agrawal on February 1, 2018
 */

class CoinInfoAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is CoinInfoModule.CoinInfoModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val coinInfoModule = CoinInfoModule()
        val coinInfoModuleView = coinInfoModule.init(LayoutInflater.from(parent.context), parent)
        return CoinInfoViewHolder(coinInfoModuleView, coinInfoModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val coinInfoViewHolder = holder as CoinInfoViewHolder
        coinInfoViewHolder.showCoinInfo((items[position] as CoinInfoModule.CoinInfoModuleData))
    }

    class CoinInfoViewHolder(itemView: View, private val coinInfoModule: CoinInfoModule) : RecyclerView.ViewHolder(itemView) {
        fun showCoinInfo(coinInfoModuleData: CoinInfoModule.CoinInfoModuleData) {
            coinInfoModule.showCoinInfo(itemView, coinInfoModuleData)
        }
    }
}
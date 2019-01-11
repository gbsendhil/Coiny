package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.CoinTransactionHistoryModule
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by Pranay Airan
 */

class CoinTransactionAdapterDelegate(private val resourceProvider: ResourceProvider) : AdapterDelegate<List<ModuleItem>>() {

    private val coinTransactionHistoryModule by lazy {
        CoinTransactionHistoryModule(resourceProvider)
    }

    override fun isForViewType(items: List<ModuleItem>, position: Int): Boolean {
        return items[position] is CoinTransactionHistoryModule.CoinTransactionHistoryModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val coinTransactionHistoryModuleView = coinTransactionHistoryModule.init(LayoutInflater.from(parent.context), parent)
        return CoinTransactionViewHolder(coinTransactionHistoryModuleView, coinTransactionHistoryModule)
    }

    override fun onBindViewHolder(items: List<ModuleItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val coinTransactionViewHolder = holder as CoinTransactionViewHolder
        coinTransactionViewHolder.showRecentTransactions((items[position] as CoinTransactionHistoryModule.CoinTransactionHistoryModuleData))
    }

    class CoinTransactionViewHolder(override val containerView: View, private val coinTransactionHistoryModule: CoinTransactionHistoryModule)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun showRecentTransactions(coinTransactionHistoryModuleData: CoinTransactionHistoryModule.CoinTransactionHistoryModuleData) {
            coinTransactionHistoryModule.showRecentTransactions(itemView, coinTransactionHistoryModuleData)
        }
    }
}
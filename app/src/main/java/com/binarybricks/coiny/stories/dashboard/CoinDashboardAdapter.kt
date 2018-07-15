package com.binarybricks.coiny.stories.dashboard

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.binarybricks.coiny.adapterdelegates.DashboardCoinAdapterDelegate
import com.binarybricks.coiny.adapterdelegates.DashboardCoinListHeaderAdapterDelegate
import com.binarybricks.coiny.adapterdelegates.DashboardEmptyCardAdapterDelegate
import com.binarybricks.coiny.adapterdelegates.DashboardHeaderAdapterDelegate
import com.binarybricks.coiny.adapterdelegates.GenericFooterAdapterDelegate
import com.binarybricks.coiny.components.ModuleItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
Created by Pranay Airan 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinDashboardAdapter(
    toCurrency: String,
    var coinDashboardList: List<ModuleItem>,
    toolbarTitle: TextView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DASHBOARD_COIN_List_HEADER = 0
    private val DASHBOARD_COIN = 1
    private val DASHBOARD_EMPTY_CARD = 2
    private val DASHBOARD_HEADER = 3
    private val DASHBOARD_FOOTER = 4

    private val delegates: AdapterDelegatesManager<List<ModuleItem>> = AdapterDelegatesManager()

    init {
        delegates.addDelegate(DASHBOARD_COIN_List_HEADER, DashboardCoinListHeaderAdapterDelegate())
        delegates.addDelegate(DASHBOARD_COIN, DashboardCoinAdapterDelegate(toCurrency))
        delegates.addDelegate(DASHBOARD_EMPTY_CARD, DashboardEmptyCardAdapterDelegate())
        delegates.addDelegate(DASHBOARD_HEADER, DashboardHeaderAdapterDelegate(toCurrency, toolbarTitle))
        delegates.addDelegate(DASHBOARD_FOOTER, GenericFooterAdapterDelegate())
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.getItemViewType(coinDashboardList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        delegates.onBindViewHolder(coinDashboardList, position, viewHolder)
    }

    override fun getItemCount(): Int {
        return coinDashboardList.size
    }
}
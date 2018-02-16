package com.binarybricks.coinhood.stories.dashboard

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.binarybricks.coinhood.adapterdelegates.DashboardCoinAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.DashboardCoinListHeaderAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.DashboardEmptyCardAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.DashboardHeaderAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager


/**
 Created by Pranay Airan 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinDashboardAdapter(toCurrency: String,
                           var coinDashboardList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DASHBOARD_COIN_List_HEADER = 0
    private val DASHBOARD_COIN = 1
    private val DASHBOARD_EMPTY_CARD = 2
    private val DASHBOARD_HEADER = 3

    private val delegates: AdapterDelegatesManager<List<Any>> = AdapterDelegatesManager()

    init {
        delegates.addDelegate(DASHBOARD_COIN_List_HEADER, DashboardCoinListHeaderAdapterDelegate())
        delegates.addDelegate(DASHBOARD_COIN, DashboardCoinAdapterDelegate(toCurrency))
        delegates.addDelegate(DASHBOARD_EMPTY_CARD, DashboardEmptyCardAdapterDelegate())
        delegates.addDelegate(DASHBOARD_HEADER, DashboardHeaderAdapterDelegate(toCurrency))
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
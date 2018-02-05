package com.binarybricks.coinhood.stories.dashboard

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.binarybricks.coinhood.adapterdelegates.DashboardCoinAdapterDelegate
import com.binarybricks.coinhood.adapterdelegates.DashboardCoinHeaderAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager


/**
 * Created by pranay airan on 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinDashboardAdapter(toCurrency: String,
                           var watchedAndPurchasedCoinsList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DASHBOARD_COIN_HEADER = 0
    private val DASHBOARD_COIN = 1

    val delegates: AdapterDelegatesManager<List<Any>> = AdapterDelegatesManager()

    init {
        delegates.addDelegate(DASHBOARD_COIN_HEADER, DashboardCoinHeaderAdapterDelegate())
        delegates.addDelegate(DASHBOARD_COIN, DashboardCoinAdapterDelegate(toCurrency))
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.getItemViewType(watchedAndPurchasedCoinsList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        delegates.onBindViewHolder(watchedAndPurchasedCoinsList, position, viewHolder)
    }

    override fun getItemCount(): Int {
        return watchedAndPurchasedCoinsList.size
    }
}
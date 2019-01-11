package com.binarybricks.coiny.stories.dashboard

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.binarybricks.coiny.adapterdelegates.*
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
Created by Pranay Airan 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinDashboardAdapter(
    toCurrency: String,
    resourceProvider: ResourceProvider,
    var coinDashboardList: MutableList<ModuleItem>,
    toolbarTitle: TextView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val DASHBOARD_COIN_List_HEADER = 0
        private const val DASHBOARD_COIN = 1
        private const val DASHBOARD_EMPTY_CARD = 2
        private const val DASHBOARD_HEADER = 3
        private const val DASHBOARD_FOOTER = 4
        private const val DASHBOARD_TOP_CAROUSAL = 5
        private const val DASHBOARD_News = 6
        private const val DASHBOARD_Add_New = 7
    }

    private val delegates: AdapterDelegatesManager<List<ModuleItem>> = AdapterDelegatesManager()

    init {
        delegates.addDelegate(DASHBOARD_COIN_List_HEADER, DashboardCoinListHeaderAdapterDelegate())
        delegates.addDelegate(DASHBOARD_COIN, DashboardCoinAdapterDelegate(toCurrency, resourceProvider))
        delegates.addDelegate(DASHBOARD_EMPTY_CARD, DashboardEmptyCardAdapterDelegate())
        delegates.addDelegate(DASHBOARD_HEADER, DashboardHeaderAdapterDelegate(toCurrency, toolbarTitle, resourceProvider))
        delegates.addDelegate(DASHBOARD_FOOTER, GenericFooterAdapterDelegate())
        delegates.addDelegate(DASHBOARD_TOP_CAROUSAL, CarousalAdapterDelegate(toCurrency, resourceProvider))
        delegates.addDelegate(DASHBOARD_News, DashboardNewsAdapterDelegate())
        delegates.addDelegate(DASHBOARD_Add_New, DashboardAddNewCoinAdapterDelegate())
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
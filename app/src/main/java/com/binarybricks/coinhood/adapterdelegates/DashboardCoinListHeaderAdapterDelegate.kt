package com.binarybricks.coinhood.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.DashboardCoinListHeaderModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pranay Airan
 * Adapter delegate that takes care of header for list on dashboard
 */

class DashboardCoinListHeaderAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DashboardCoinListHeaderModule.DashboardCoinListHeaderModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardCoinListHeaderModule = DashboardCoinListHeaderModule()
        val dashboardCoinHeaderModuleView = dashboardCoinListHeaderModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardCoinListHeaderViewHolder(dashboardCoinHeaderModuleView, dashboardCoinListHeaderModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val dashboardCoinHeaderViewHolder = holder as DashboardCoinListHeaderViewHolder
        dashboardCoinHeaderViewHolder.showHeaderText((items[position] as DashboardCoinListHeaderModule.DashboardCoinListHeaderModuleData).title)
    }

    class DashboardCoinListHeaderViewHolder(itemView: View, private val dashboardCoinListHeaderModule: DashboardCoinListHeaderModule) : RecyclerView.ViewHolder(itemView) {
        fun showHeaderText(title: String) {
            dashboardCoinListHeaderModule.showHeaderText(itemView, title)
        }
    }
}
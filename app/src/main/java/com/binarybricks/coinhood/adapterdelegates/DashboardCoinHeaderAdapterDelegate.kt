package com.binarybricks.coinhood.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.DashboardCoinHeaderModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pranay Airan
 * Adapter delegate that takes care of header sections on dashboard
 */

class DashboardCoinHeaderAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DashboardCoinHeaderModule.DashboardCoinHeaderModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardCoinHeaderModule = DashboardCoinHeaderModule()
        val dashboardCoinHeaderModuleView = dashboardCoinHeaderModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardCoinHeaderViewHolder(dashboardCoinHeaderModuleView, dashboardCoinHeaderModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val dashboardCoinHeaderViewHolder = holder as DashboardCoinHeaderViewHolder
        dashboardCoinHeaderViewHolder.showHeaderText((items[position] as DashboardCoinHeaderModule.DashboardCoinHeaderModuleData).title)
    }

    class DashboardCoinHeaderViewHolder(itemView: View, private val dashboardCoinHeaderModule: DashboardCoinHeaderModule) : RecyclerView.ViewHolder(itemView) {
        fun showHeaderText(title: String) {
            dashboardCoinHeaderModule.showHeaderText(itemView, title)
        }
    }
}
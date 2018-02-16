package com.binarybricks.coinhood.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.DashboardCoinModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pranay Airan
 * Adapter delegate that takes care of coin row in dashboard.
 */

class DashboardCoinAdapterDelegate(private val toCurrency: String) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DashboardCoinModule.DashboardCoinModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardCoinModule = DashboardCoinModule(toCurrency)
        val dashboardCoinModuleView = dashboardCoinModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardCoinViewHolder(dashboardCoinModuleView, dashboardCoinModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val aboutCoinViewHolder = holder as DashboardCoinViewHolder
        aboutCoinViewHolder.showCoinInfo(items[position] as DashboardCoinModule.DashboardCoinModuleData)
    }

    class DashboardCoinViewHolder(itemView: View, private val dashboardCoinModule: DashboardCoinModule) : RecyclerView.ViewHolder(itemView) {
        fun showCoinInfo(dashboardCoinModuleData: DashboardCoinModule.DashboardCoinModuleData) {
            dashboardCoinModule.showCoinInfo(itemView, dashboardCoinModuleData)
        }
    }
}
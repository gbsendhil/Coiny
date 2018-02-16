package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.DashboardHeaderModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pranay Airan
 */

class DashboardHeaderAdapterDelegate(private val toCurrency: String) : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DashboardHeaderModule.DashboardHeaderModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardHeaderModule = DashboardHeaderModule(toCurrency)
        val dashboardHeaderModuleView = dashboardHeaderModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardHeaderViewHolder(dashboardHeaderModuleView, dashboardHeaderModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val dashboardHeaderViewHolder = holder as DashboardHeaderViewHolder
        dashboardHeaderViewHolder.loadPortfolio()
    }

    class DashboardHeaderViewHolder(itemView: View, private val dashboardHeaderModule: DashboardHeaderModule) : RecyclerView.ViewHolder(itemView) {
        fun loadPortfolio() {
            dashboardHeaderModule.loadPortfolioData(itemView)
        }
    }
}
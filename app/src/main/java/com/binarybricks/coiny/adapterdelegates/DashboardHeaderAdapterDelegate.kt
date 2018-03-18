package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binarybricks.coiny.components.DashboardHeaderModule
import com.binarybricks.coiny.components.ModuleItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer


/**
 * Created by Pranay Airan
 */

class DashboardHeaderAdapterDelegate(private val toCurrency: String, private val toolbarTitle: TextView) : AdapterDelegate<List<ModuleItem>>() {

    override fun isForViewType(items: List<ModuleItem>, position: Int): Boolean {
        return items[position] is DashboardHeaderModule.DashboardHeaderModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardHeaderModule = DashboardHeaderModule(toCurrency, toolbarTitle)
        val dashboardHeaderModuleView = dashboardHeaderModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardHeaderViewHolder(dashboardHeaderModuleView, dashboardHeaderModule)
    }

    override fun onBindViewHolder(items: List<ModuleItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val dashboardHeaderViewHolder = holder as DashboardHeaderViewHolder
        dashboardHeaderViewHolder.loadPortfolio()
    }

    class DashboardHeaderViewHolder(override val containerView: View, private val dashboardHeaderModule: DashboardHeaderModule)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun loadPortfolio() {
            dashboardHeaderModule.loadPortfolioData(itemView)
        }
    }
}
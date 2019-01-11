package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binarybricks.coiny.components.DashboardHeaderModule
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by Pranay Airan
 */

class DashboardHeaderAdapterDelegate(
    private val toCurrency: String,
    private val toolbarTitle: TextView,
    private val resourceProvider: ResourceProvider
) : AdapterDelegate<List<ModuleItem>>() {

    private val dashboardHeaderModule by lazy {
        DashboardHeaderModule(toCurrency, toolbarTitle, resourceProvider)
    }

    override fun isForViewType(items: List<ModuleItem>, position: Int): Boolean {
        return items[position] is DashboardHeaderModule.DashboardHeaderModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardHeaderModuleView = dashboardHeaderModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardHeaderViewHolder(dashboardHeaderModuleView, dashboardHeaderModule)
    }

    override fun onBindViewHolder(items: List<ModuleItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val dashboardHeaderViewHolder = holder as DashboardHeaderViewHolder
        dashboardHeaderViewHolder.loadPortfolio(items[position] as DashboardHeaderModule.DashboardHeaderModuleData)
    }

    class DashboardHeaderViewHolder(override val containerView: View, private val dashboardHeaderModule: DashboardHeaderModule)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun loadPortfolio(dashboardHeaderModuleData: DashboardHeaderModule.DashboardHeaderModuleData) {
            dashboardHeaderModule.loadPortfolioData(itemView, dashboardHeaderModuleData)
        }
    }
}
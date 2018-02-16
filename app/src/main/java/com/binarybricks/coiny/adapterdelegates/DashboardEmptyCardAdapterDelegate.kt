package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.DashboardEmptyCardModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pranay Airan
 */

class DashboardEmptyCardAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is DashboardEmptyCardModule.DashboardEmptyCardModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val dashboardEmptyCardModule = DashboardEmptyCardModule()
        val dashboardEmptyCardModuleView = dashboardEmptyCardModule.init(LayoutInflater.from(parent.context), parent)
        return DashboardEmptyCardViewHolder(dashboardEmptyCardModuleView, dashboardEmptyCardModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val dashboardEmptyCardViewHolder = holder as DashboardEmptyCardViewHolder
        dashboardEmptyCardViewHolder.showTextInEmptySpace((items[position] as DashboardEmptyCardModule.DashboardEmptyCardModuleData).emptySpaceText)
    }

    class DashboardEmptyCardViewHolder(itemView: View, private val dashboardEmptyCardModule: DashboardEmptyCardModule) : RecyclerView.ViewHolder(itemView) {
        fun showTextInEmptySpace(emptySpaceText: String) {
            dashboardEmptyCardModule.showTextInEmptySpace(itemView, emptySpaceText)
        }
    }
}
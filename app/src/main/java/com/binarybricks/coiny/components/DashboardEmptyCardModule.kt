package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.dashboard_empty_card_module.view.*
import timber.log.Timber

/**
 * Created by Pranay Airan
 *
 * Simple class that shows the empty card on dashboard
 */

class DashboardEmptyCardModule : Module() {

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_empty_card_module, parent, false)
    }

    fun showTextInEmptySpace(inflatedView: View, dashboardEmptyCardModuleData: DashboardEmptyCardModuleData) {
        inflatedView.tvEmptyMessage.text = dashboardEmptyCardModuleData.emptySpaceText
    }

    override fun cleanUp() {
        Timber.d("Clean up empty coinSymbol module")
    }

    data class DashboardEmptyCardModuleData(val emptySpaceText: String) : ModuleItem
}
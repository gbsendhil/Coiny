package com.binarybricks.coinhood.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import kotlinx.android.synthetic.main.dashboard_empty_card_module.view.*

/**
 * Created by Pranay Airan
 *
 * Simple class that shows the empty card on dashboard
 */

class DashboardEmptyCardModule {

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_empty_card_module, parent, false)
    }

    fun showTextInEmptySpace(inflatedView: View, emptyText: String) {
        inflatedView.tvEmptyMessage.text = emptyText
    }

    data class DashboardEmptyCardModuleData(val emptySpaceText: String)
}
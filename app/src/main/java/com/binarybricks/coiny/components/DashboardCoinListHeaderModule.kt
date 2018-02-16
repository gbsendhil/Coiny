package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.dashboard_coin_list_header_module.view.*

/**
 * Created by Pranay Airan
 */

class DashboardCoinListHeaderModule {
    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_coin_list_header_module, parent, false)
    }

    fun showHeaderText(inflatedView: View, title: String) {
        inflatedView.tvLabel.text = title
    }

    data class DashboardCoinListHeaderModuleData(val title: String)
}
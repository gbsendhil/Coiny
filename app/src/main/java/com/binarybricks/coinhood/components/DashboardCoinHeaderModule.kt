package com.binarybricks.coinhood.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import kotlinx.android.synthetic.main.dashboard_coin_header_module.view.*

/**
 * Created by pranay airan on 1/19/18.
 */

class DashboardCoinHeaderModule {
    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_coin_header_module, parent, false)
    }

    fun showHeaderText(inflatedView: View, title: String) {
        inflatedView.tvLabel.text = title
    }

    data class DashboardCoinHeaderModuleData(val title: String)
}
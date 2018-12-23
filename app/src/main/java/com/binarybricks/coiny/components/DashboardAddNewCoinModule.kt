package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import timber.log.Timber

/**
 * Created by Pranay Airan
 *
 * Simple class that shows the add new coin card on dashboard
 */

class DashboardAddNewCoinModule : Module() {

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_new_coin_module, parent, false)
    }

    override fun cleanUp() {
        Timber.d("Clean up empty DashboardAddNewCoin module")
    }

    data class DashboardAddNewCoinModuleData(val noText: String = "") : ModuleItem
}
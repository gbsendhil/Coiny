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

    interface OnAddItemClickListener {
        fun onAddNewCoinClicked()
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_new_coin_module, parent, false)
    }

    fun addNewCoinListener(inflatedView: View, dashboardAddNewCoinModuleData: DashboardAddNewCoinModuleData) {

        inflatedView.findViewById<View>(R.id.addCoinCard).setOnClickListener {
            dashboardAddNewCoinModuleData.onAddItemClickListener.onAddNewCoinClicked()
        }
    }

    override fun cleanUp() {
        Timber.d("Clean up empty DashboardAddNewCoin module")
    }

    data class DashboardAddNewCoinModuleData(val onAddItemClickListener: OnAddItemClickListener) : ModuleItem
}
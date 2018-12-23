package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.carousal_module.view.*
import timber.log.Timber

/**
 * Created by Pranay Airan
 *
 * Simple class that takes list of items and show them on in horizontal carousal.
 */

class CarousalModule : Module() {

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.carousal_module, parent, false)
    }

    fun addCarousalModule(layoutInflater: LayoutInflater, parent: ViewGroup?, inflatedView: View, dashboardEmptyCoinModuleData: CarousalModuleData) {
        if (dashboardEmptyCoinModuleData.carousalItems != null) {
            inflatedView.pbLoading.visibility = View.GONE

            dashboardEmptyCoinModuleData.carousalItems.forEach {
                if (it is TopCard.TopCardsModuleData) {
                    val topCard = TopCard()
                    val topCardInflatedView = topCard.init(layoutInflater, parent)
                    topCard.addTopCardModule(topCardInflatedView, it)
                    inflatedView.llCarousal.addView(topCardInflatedView)
                }
            }
        }
    }

    override fun cleanUp() {
        Timber.d("Clean up empty coinSymbol module")
    }

    data class CarousalModuleData(val carousalItems: List<ModuleItem>?) : ModuleItem
}
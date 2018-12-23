package com.binarybricks.coiny.components


import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.top_card_module.view.*
import timber.log.Timber

/**
 * Created by Pranay Airan
 *
 * Simple class wrapping UI for top card
 */
class TopCard : Module() {

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.top_card_module, parent, false)
    }

    fun addTopCardModule(inflatedView: View, topCardsModuleData: TopCardsModuleData) {

        inflatedView.tvPair.text = topCardsModuleData.pair
        inflatedView.tvPrice.text = topCardsModuleData.price
        inflatedView.tvPriceChange.text = ("%.2f".format(topCardsModuleData.priceChangePercentage.toDouble()))
        inflatedView.tvMarketCap.text = "Mkt cap: ${topCardsModuleData.marketCap}"

        try {
            if (topCardsModuleData.priceChangePercentage.toDouble() < 0) {
                inflatedView.tvPrice.setTextColor(ContextCompat.getColor(inflatedView.context, R.color.colorLoss))
                inflatedView.tvPriceChange.setTextColor(ContextCompat.getColor(inflatedView.context, R.color.colorLoss))
            }
        } catch (ex: NumberFormatException) {
            Timber.e(ex)
        }
    }

    override fun cleanUp() {
        Timber.d("Clean up empty TopCard module")
    }

    data class TopCardsModuleData(val pair: String, val price: String, val priceChangePercentage: String, val marketCap: String) : ModuleItem

}
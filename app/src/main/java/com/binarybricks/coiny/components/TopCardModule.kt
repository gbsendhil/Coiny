package com.binarybricks.coiny.components

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.stories.coindetails.CoinDetailsActivity
import com.binarybricks.coiny.utils.CurrencyUtils
import com.binarybricks.coiny.utils.ResourceProvider
import kotlinx.android.synthetic.main.top_card_module.view.*
import timber.log.Timber
import java.math.BigDecimal
import java.util.*

/**
 * Created by Pranay Airan
 *
 * Simple class wrapping UI for top card
 */
class TopCardModule(
    private val toCurrency: String,
    private val resourceProvider: ResourceProvider
) : Module() {

    private val currency by lazy {
        Currency.getInstance(toCurrency)
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.top_card_module, parent, false)
    }

    fun addTopCardModule(inflatedView: View, topCardsModuleData: TopCardsModuleData) {

        inflatedView.tvPair.text = topCardsModuleData.pair
        inflatedView.tvPrice.text = topCardsModuleData.price
        inflatedView.tvPriceChange.text = resourceProvider.getString(R.string.coinDayChanges,
                topCardsModuleData.priceChangePercentage.toDouble())

        inflatedView.tvMarketCap.text = resourceProvider.getString(R.string.marketCap,
                CurrencyUtils.getNaturalTextForDisplay(BigDecimal(topCardsModuleData.marketCap), currency))

        inflatedView.topCardContainer.setOnClickListener {
            inflatedView.context.startActivity(CoinDetailsActivity.buildLaunchIntent(inflatedView.context,
                    topCardsModuleData.coinSymbol))
        }

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
        Timber.d("Clean up empty TopCardModule module")
    }

    data class TopCardsModuleData(
        val pair: String,
        val price: String,
        val priceChangePercentage: String,
        val marketCap: String,
        val coinSymbol: String
    ) : ModuleItem
}
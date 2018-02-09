package com.binarybricks.coinhood.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.data.PreferenceHelper
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.utils.Formatters
import kotlinx.android.synthetic.main.coin_statistic_module.view.*
import java.util.*

/**
 * @author Pragya Agrawal on February 1, 2018
 *
 * Simple class that wraps all logic related to Coin stats
 */

class CoinStatsticsModule {

    private val formatter by lazy {
        Formatters()
    }

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_statistic_module, parent, false)
    }

    fun showCoinStats(inflatedView: View, coinPrice: CoinPrice) {
        val currency = Currency.getInstance(PreferenceHelper.getDefaultCurrency(inflatedView.context))

        inflatedView.tvOpenAmount.text = formatter.formatAmount(coinPrice.openDay
                ?: "0", currency, true)
        inflatedView.tvTodaysHighAmount.text = formatter.formatAmount(coinPrice.highDay
                ?: "0", currency, true)
        inflatedView.tvTodayLowAmount.text = formatter.formatAmount(coinPrice.lowDay
                ?: "0", currency, true)
        inflatedView.tvChangeTodayAmount.text = formatter.formatAmount(coinPrice.changeDay
                ?: "0", currency, true)

        inflatedView.tvVolumeQuantity.text = formatter.formatAmount(coinPrice.volumneDay
                ?: "0", currency, true)
        inflatedView.tvAvgVolumeAmount.text = formatter.formatAmount(coinPrice.totalVolume24Hour
                ?: "0", currency, true)

        inflatedView.tvAvgMarketCapAmount.text = formatter.formatAmount(coinPrice.marketCap
                ?: "0", currency, true)

        inflatedView.tvSupplyNumber.text = "${formatter.formatNumber(coinPrice.supply
                ?: 0)} ${coinPrice.fromSymbol}"
    }

    data class CoinStatsticsModuleData(val coinPrice: CoinPrice)
}
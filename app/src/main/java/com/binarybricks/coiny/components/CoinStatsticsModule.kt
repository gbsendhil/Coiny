package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.utils.Formatters
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvAvgMarketCapAmount
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvAvgVolumeAmount
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvChangeTodayAmount
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvOpenAmount
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvSupplyNumber
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvTodayLowAmount
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvTodaysHighAmount
import kotlinx.android.synthetic.main.coin_statistic_module.view.tvVolumeQuantity
import timber.log.Timber
import java.util.Currency

/**
 * Created by Pragya Agrawal
 *
 * Simple class that wraps all logic related to Coin stats
 */

class CoinStatsticsModule : Module() {

    private val formatter by lazy {
        Formatters()
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_statistic_module, parent, false)
    }

    fun showCoinStats(inflatedView: View, coinStatisticsModuleData: CoinStatisticsModuleData) {
        val currency = Currency.getInstance(PreferenceHelper.getDefaultCurrency(inflatedView.context))
        val coinPrice = coinStatisticsModuleData.coinPrice

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
                ?: "0", currency, false)

        inflatedView.tvSupplyNumber.text = "${formatter.formatNumber(coinPrice.supply
                ?: 0)} ${coinPrice.fromSymbol}"
    }

    override fun cleanUp() {
        Timber.d("Clean up coinSymbol stats module")
    }

    data class CoinStatisticsModuleData(val coinPrice: CoinPrice) : ModuleItem
}
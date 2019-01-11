package com.binarybricks.coiny.components

import android.animation.ValueAnimator
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.utils.Formatters
import com.binarybricks.coiny.utils.ResourceProvider
import com.binarybricks.coiny.utils.chartAnimationDuration
import com.binarybricks.coiny.utils.getTotalCost
import kotlinx.android.synthetic.main.dashboard_header_module.view.*
import timber.log.Timber
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Currency
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Created by Pranay Airan
 *
 * Simple class that wraps all logic related to showing header on dashboard
 */

class DashboardHeaderModule(
    private val toCurrency: String,
    private val toolbarTitle: TextView,
    private val resourceProvider: ResourceProvider
) : Module() {

    private lateinit var inflatedView: View
    private val purchasedCoinList: MutableList<WatchedCoin> = ArrayList()

    private val currency by lazy {
        Currency.getInstance(toCurrency)
    }

    private val formatter by lazy {
        Formatters(resourceProvider)
    }

    private val mc by lazy {
        MathContext(2, RoundingMode.HALF_UP)
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_header_module, parent, false)
    }

    fun loadPortfolioData(inflatedView: View, dashboardHeaderModuleData: DashboardHeaderModule.DashboardHeaderModuleData) {
        this.inflatedView = inflatedView

        // when the view is loaded first time we are still waiting for prices to be fetched.
        if (dashboardHeaderModuleData.coinPriceListMap.isNotEmpty()) {

            val coinPriceListMap = dashboardHeaderModuleData.coinPriceListMap
            // get the total value of coins
            var portfolioValue = BigDecimal.ZERO
            var totalPortfolioCost = BigDecimal.ZERO

            purchasedCoinList.forEach {
                if (coinPriceListMap.containsKey(it.coin.symbol.toUpperCase())) {
                    portfolioValue = portfolioValue.add(BigDecimal(coinPriceListMap[it.coin.symbol.toUpperCase()]?.price).multiply(it.purchaseQuantity))
                    totalPortfolioCost = totalPortfolioCost.add(getTotalCost(dashboardHeaderModuleData.coinTransactionList, it.coin.symbol))
                }
            }

            animateCoinPrice(portfolioValue.toPlainString())
            toolbarTitle.text = formatter.formatAmount(portfolioValue.toPlainString(), currency)

            // do the profit or loss things here.
            val totalReturnAmount = portfolioValue.subtract(totalPortfolioCost)
            val totalReturnPercentage = BigDecimal.ZERO
            if (totalReturnAmount > BigDecimal.ZERO) {
                (totalReturnAmount.divide(totalPortfolioCost, mc))?.multiply(BigDecimal(100), mc)
            }

            if (totalReturnAmount != null) {
                inflatedView.tvPortfolioChangedValue.text = formatter.formatAmount(totalReturnAmount.toPlainString(), currency)
                inflatedView.tvPortfolioChangedPercentage.text = inflatedView.context.getString(R.string.portfolio_changed_percentage, totalReturnPercentage.toString())
            }

            if (totalReturnAmount != null && totalReturnAmount < BigDecimal.ZERO) {
                inflatedView.tvPortfolioChangedValue.setTextColor(ContextCompat.getColor(inflatedView.context, R.color.colorLoss))
                inflatedView.tvPortfolioChangedPercentage.setTextColor(ContextCompat.getColor(inflatedView.context, R.color.colorLoss))
            } else {
                inflatedView.tvPortfolioChangedValue.setTextColor(ContextCompat.getColor(inflatedView.context, R.color.colorGain))
                inflatedView.tvPortfolioChangedPercentage.setTextColor(ContextCompat.getColor(inflatedView.context, R.color.colorGain))
            }
        } else {
            // get the coins that are purchased
            dashboardHeaderModuleData.watchedCoinList.forEach {
                if (it.purchaseQuantity > BigDecimal.ZERO) {
                    purchasedCoinList.add(it)
                }
            }
        }
    }

    override fun cleanUp() {
        Timber.d("Clean up header module")
    }

    private fun animateCoinPrice(amount: String?) {
        if (amount != null) {
            val chartCoinPriceAnimation = ValueAnimator.ofFloat(inflatedView.tvPortfolioValue.tag.toString().toFloat(), amount.toFloat())
            chartCoinPriceAnimation.duration = chartAnimationDuration
            chartCoinPriceAnimation.addUpdateListener { updatedAnimation ->
                val animatedValue = updatedAnimation.animatedValue as Float
                inflatedView.tvPortfolioValue.text = formatter.formatAmount(animatedValue.toString(), currency)
                inflatedView.tvPortfolioValue.tag = animatedValue
            }
            chartCoinPriceAnimation.start()
        }
    }

    data class DashboardHeaderModuleData(
        var watchedCoinList: List<WatchedCoin>,
        var coinTransactionList: List<CoinTransaction>,
        var coinPriceListMap: HashMap<String, CoinPrice>
    ) : ModuleItem
}
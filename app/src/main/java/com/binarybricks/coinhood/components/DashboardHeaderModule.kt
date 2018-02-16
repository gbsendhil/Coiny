package com.binarybricks.coinhood.components

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.utils.Formatters
import com.binarybricks.coinhood.utils.chartAnimationDuration
import kotlinx.android.synthetic.main.dashboard_header_module.view.*
import java.util.*

/**
 * Created by Pranay Airan
 *
 * Simple class that wraps all logic related to showing header on dashboard
 */

class DashboardHeaderModule(private val toCurrency: String) {

    private lateinit var inflatedView: View

    private val currency by lazy {
        Currency.getInstance(toCurrency)
    }

    private val formatter by lazy {
        Formatters()
    }

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_header_module, parent, false)
    }

    fun loadPortfolioData(inflatedView: View) {
        this.inflatedView = inflatedView

        animateCoinPrice("10")
        inflatedView.tvPortfolioChangedValue.text = formatter.formatAmount("0.00", currency)
    }

    class DashboardHeaderModuleData

    private fun animateCoinPrice(amount: String?) {
        if (amount != null) {
            val chartCoinPriceAnimation = ValueAnimator.ofFloat(inflatedView.tvPortfolioValue.tag.toString().toFloat(), amount.toFloat())
            chartCoinPriceAnimation.duration = chartAnimationDuration
            chartCoinPriceAnimation.addUpdateListener({ updatedAnimation ->
                val animatedValue = updatedAnimation.animatedValue as Float
                inflatedView.tvPortfolioValue.text = formatter.formatAmount(animatedValue.toString(), currency)
                //inflatedView.tvPortfolioValue.tag = animatedValue
            })
            chartCoinPriceAnimation.start()
        }
    }
}
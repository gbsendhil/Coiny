package com.binarybricks.coinhood.components.historicalchartmodule

import HistoricalChartContract
import android.animation.ValueAnimator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.*
import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.Formatters
import com.binarybricks.coinhood.utils.ResourceProvider
import com.binarybricks.coinhood.utils.changeChildrenColor
import com.binarybricks.coinhood.utils.chartAnimationDuration
import kotlinx.android.synthetic.main.historical_chart_module.view.*
import java.util.*

/**
Created by Pranay Airan 1/10/18.
 * A compound layout to see historical charts.
 */
class HistoricalChartModule(private val schedulerProvider: BaseSchedulerProvider,
                            private val resourceProvider: ResourceProvider, private val fromCurrency: String,
                            private val toCurrency: String) : LifecycleObserver, HistoricalChartContract.View {

    private lateinit var inflatedView: View

    private var historicalData: List<CryptoCompareHistoricalResponse.Data>? = null
    private var coinPrice: CoinPrice? = null

    private var selectedPeriod = HOUR

    private val currency by lazy {
        Currency.getInstance(toCurrency)
    }

    private val formatter by lazy {
        Formatters()
    }

    private val historicalChatPresenter: HistoricalChartPresenter by lazy {
        HistoricalChartPresenter(schedulerProvider)
    }

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {

        val inflatedView = layoutInflater.inflate(R.layout.historical_chart_module, parent, false)

        historicalChatPresenter.attachView(this)

        return inflatedView
    }

    fun loadData(inflatedView: View) {
        this.inflatedView = inflatedView
        historicalChatPresenter.loadHistoricalData(HOUR, fromCurrency, toCurrency)
        addChartScrubListener()
        addRangeSelectorListener()
    }

    override fun addCoinAndAnimateCoinPrice(coinPrice: CoinPrice?) {
        this.coinPrice = coinPrice
        animateCoinPrice(coinPrice?.price)
    }

    override fun showOrHideChartLoadingIndicator(showLoading: Boolean) {
        if (showLoading) inflatedView.pbChartLoading.show() else inflatedView.pbChartLoading.hide()
    }

    override fun onHistoricalDataLoaded(period: String,
                                        dataListPair: Pair<List<CryptoCompareHistoricalResponse.Data>, CryptoCompareHistoricalResponse.Data?>) {

        historicalData = dataListPair.first

        inflatedView.historicalChartView.adapter =
                HistoricalChartAdapter(dataListPair.first, dataListPair.second?.open)

        if (period != ALL) {
            showPercentageGainOrLoss(dataListPair.first)
        } else {
            inflatedView.tvPortfolioChangedValue.text = ""
            showPositiveGainColor()
        }
        showChartPeriodText(period)
    }

    private fun showPercentageGainOrLoss(
        historicalData: List<CryptoCompareHistoricalResponse.Data>?) {
        if (historicalData != null) {
            val lastClosingPrice =
                historicalData.first().close.toFloat() // we always get's oldest first in api
            val currentClosingPrice = historicalData.last().close.toFloat()
            val gain = currentClosingPrice - lastClosingPrice
            val percentageChange: Float = (gain / lastClosingPrice) * 100
            inflatedView.tvPortfolioChangedValue.text =
                    resourceProvider.getString(R.string.gain, percentageChange,
                        formatter.formatAmount(gain.toString(), currency))
            if (gain > 0) {
                showPositiveGainColor()
            } else {
                showNegativeGainColor()
            }
        }
    }

    private fun showPositiveGainColor() {
        inflatedView.tvPortfolioChangedValue.setTextColor(
            resourceProvider.getColor(R.color.colorPrimary))
        inflatedView.historicalChartView.lineColor = resourceProvider.getColor(R.color.colorPrimary)
        inflatedView.rgPeriodSelector.changeChildrenColor(
            resourceProvider.getColor(R.color.colorPrimary))
    }

    private fun showNegativeGainColor() {
        inflatedView.tvPortfolioChangedValue.setTextColor(
            resourceProvider.getColor(R.color.colorSecondary))
        inflatedView.historicalChartView.lineColor =
                resourceProvider.getColor(R.color.colorSecondary)
        inflatedView.rgPeriodSelector.changeChildrenColor(
            resourceProvider.getColor(R.color.colorSecondary))
    }

    private fun showChartPeriodText(period: String) {
        val periodText = when (period) {
            HOUR -> resourceProvider.getString(R.string.past_hour)
            HOURS24 -> resourceProvider.getString(R.string.past_day)
            WEEK -> resourceProvider.getString(R.string.past_week)
            MONTH -> resourceProvider.getString(R.string.past_month)
            YEAR -> resourceProvider.getString(R.string.past_year)
            ALL -> resourceProvider.getString(R.string.all_time)
            else -> resourceProvider.getString(R.string.past_hour)
        }
        inflatedView.tvPortfolioChangedPercentage.text = periodText
    }

    private fun addChartScrubListener() {
        inflatedView.historicalChartView.setScrubListener { value ->
            if (value == null) { // reset the amount
                animateCoinPrice(coinPrice?.price)
                showPercentageGainOrLoss(historicalData)
                showChartPeriodText(selectedPeriod)
            } else {
                val historicalData = value as CryptoCompareHistoricalResponse.Data
                inflatedView.tvPortfolioChangedValue.text = ""
                inflatedView.tvPortfolioChangedPercentage.text = formatter.formatDate(historicalData.time, 1000)
                animateCoinPrice(historicalData.close)
            }
        }
    }

    private fun animateCoinPrice(amount: String?) {
        if (amount != null) {
            val chartCoinPriceAnimation = ValueAnimator.ofFloat(inflatedView.tvChartCoinPrice.tag.toString().toFloat(), amount.toFloat())
            chartCoinPriceAnimation.duration = chartAnimationDuration
            chartCoinPriceAnimation.addUpdateListener({ updatedAnimation ->
                val animatedValue = updatedAnimation.animatedValue as Float
                inflatedView.tvChartCoinPrice.text =
                        formatter.formatAmount(animatedValue.toString(), currency)
                inflatedView.tvChartCoinPrice.tag = animatedValue
            })
            chartCoinPriceAnimation.start()
        }
    }

    private fun addRangeSelectorListener() {
        inflatedView.rgPeriodSelector.setOnCheckedChangeListener({ _, id ->
            val period = when (id) {
                R.id.rbPeriod1H -> HOUR
                R.id.rbPeriod1D -> HOURS24
                R.id.rbPeriod1W -> WEEK
                R.id.rbPeriod1M -> MONTH
                R.id.rbPeriod1Y -> YEAR
                R.id.rbPeriodAll -> ALL
                else -> HOUR
            }
            inflatedView.findViewById<RadioButton>(id)
                .setTextColor(resourceProvider.getColor(R.color.primaryTextColor))
            selectedPeriod = period

            historicalChatPresenter.loadHistoricalData(period, fromCurrency, toCurrency)
        })
    }

    // cleanup
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanYourSelf() {
        historicalChatPresenter.detachView()
        historicalData = null
        coinPrice = null
    }

    override fun onNetworkError(errorMessage: String) {
        Snackbar.make(inflatedView, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    data class HistoricalChartModuleData(val coinPriceWithCurrentPrice: CoinPrice?)
}
package com.binarybricks.coinhood.stories.sparkchart

import android.animation.ValueAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RadioButton
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.*
import com.binarybricks.coinhood.network.models.Coin
import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.CoinRepository
import com.binarybricks.coinhood.utils.Formatters
import com.binarybricks.coinhood.utils.changeChildrenColor
import com.binarybricks.coinhood.utils.chartAnimationDuration
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.historical_chart_module.view.*
import timber.log.Timber
import java.util.*


/**
 * Created by pranay airan on 1/10/18.
 * A compound layout to see historical charts.
 */
// TODO change this to card view and make it as component without extending constrain layout
// TODO make it lifecycle aware once you make it as component and dispose the stream
// TODO try to move it from late init
// TODO lot more things happening on UI thread, let's fix that.

class HistoricalChartModule : ConstraintLayout {

    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var fromCurrency: String
    private lateinit var toCurrency: String

    private var historicalData: List<CryptoCompareHistoricalResponse.Data>? = null
    private var coin: Coin? = null

    private var selectedPeriod = HOUR

    constructor(context: android.content.Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val chatRepo by lazy {
        ChartRepository(schedulerProvider)
    }

    private val coinRepo by lazy {
        CoinRepository(schedulerProvider)
    }

    private val currency by lazy {
        Currency.getInstance("USD")
    }

    private val formatter by lazy {
        Formatters()
    }

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun init(schedulerProvider: BaseSchedulerProvider, fromCurrency: String, toCurrency: String) {

        this.schedulerProvider = schedulerProvider

        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.historical_chart_module, this)

        this.fromCurrency = fromCurrency
        this.toCurrency = toCurrency

        loadCurrentCoinPrice(fromCurrency, toCurrency)
        loadHistoricalData(HOUR, fromCurrency, toCurrency)
        addChartScrubListener()
        addRangeSelectorListener()
    }

    private fun loadCurrentCoinPrice(fromCurrency: String, toCurrency: String) {
        coinRepo.getSingleCoinPrice(fromCurrency, toCurrency)
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    if (it.size > 0) coin = it[0]
                    animateCoinPrice(coin?.price)
                }, {

                })
    }

    private fun loadHistoricalData(period: String, fromCurrency: String, toCurrency: String) {
        pbChartLoading.show()
        compositeDisposable.add(chatRepo.getCryptoHistoricalData(period, fromCurrency, toCurrency)
                .observeOn(schedulerProvider.ui())
                .doFinally({ pbChartLoading.hide() })
                .subscribe({ dataList ->
                    if (dataList.isNotEmpty()) {
                        Timber.d("Data fetched with size ${dataList.size}")
                        historicalData = dataList
                        val maxClosingValueFromHistoricalData = dataList.maxBy { it.close.toFloat() }
                        historicalChartView.adapter = HistoricalChartAdapter(dataList, maxClosingValueFromHistoricalData?.close)
                        if (period != ALL) {
                            showPercentageGainOrLoss(dataList)
                        } else {
                            tvChartPercentageChange.text = ""
                            showPositiveGainColor()
                        }
                        showChartPeriodText(period)
                    }
                }, {
                    Timber.e(it.localizedMessage)
                }))
    }

    private fun showPercentageGainOrLoss(historicalData: List<CryptoCompareHistoricalResponse.Data>?) {
        if (historicalData != null) {
            val lastClosingPrice = historicalData.first().close.toFloat() // we always get's oldest first in api
            val currentClosingPrice = historicalData.last().close.toFloat()
            val gain = currentClosingPrice - lastClosingPrice
            val percentageChange: Float = (gain / lastClosingPrice) * 100
            tvChartPercentageChange.text = context.getString(R.string.gain, percentageChange, formatter.formatAmount(gain.toString(), currency))
            if (gain > 0) {
                showPositiveGainColor()
            } else {
                showNegativeGainColor()
            }
        }
    }

    private fun showPositiveGainColor() {
        tvChartPercentageChange.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        historicalChartView.lineColor = ContextCompat.getColor(context, R.color.colorPrimary)
        rgPeriodSelector.changeChildrenColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    private fun showNegativeGainColor() {
        tvChartPercentageChange.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary))
        historicalChartView.lineColor = ContextCompat.getColor(context, R.color.colorSecondary)
        rgPeriodSelector.changeChildrenColor(ContextCompat.getColor(context, R.color.colorSecondary))
    }

    private fun showChartPeriodText(period: String) {
        val periodText = when (period) {
            HOUR -> context.getString(R.string.past_hour)
            HOURS24 -> context.getString(R.string.past_day)
            WEEK -> context.getString(R.string.past_week)
            MONTH -> context.getString(R.string.past_month)
            YEAR -> context.getString(R.string.past_year)
            ALL -> context.getString(R.string.all_time)
            else -> context.getString(R.string.past_hour)
        }
        tvChartPeriod.text = periodText
    }

    private fun addChartScrubListener() {
        historicalChartView.setScrubListener { value ->
            if (value == null) {
                // reset the amount
                animateCoinPrice(coin?.price)
                showPercentageGainOrLoss(historicalData)
                showChartPeriodText(selectedPeriod)
            } else {
                val historicalData = value as CryptoCompareHistoricalResponse.Data
                tvChartPercentageChange.text = ""
                tvChartPeriod.text = formatter.formatDate(historicalData.time, 1000)
                animateCoinPrice(historicalData.close)
            }
        }
    }

    private fun animateCoinPrice(amount: String?) {
        if (amount != null) {
            val chartCoinPriceAnimation = ValueAnimator.ofFloat(tvChartCoinPrice.tag.toString().toFloat(), amount.toFloat())
            chartCoinPriceAnimation.duration = chartAnimationDuration
            chartCoinPriceAnimation.addUpdateListener({ updatedAnimation ->
                val animatedValue = updatedAnimation.animatedValue as Float
                tvChartCoinPrice.text = formatter.formatAmount(animatedValue.toString(), currency)
                tvChartCoinPrice.tag = animatedValue
            })
            chartCoinPriceAnimation.start()
        }
    }


    private fun addRangeSelectorListener() {
        rgPeriodSelector.setOnCheckedChangeListener({ _, id ->
            val period = when (id) {
                R.id.rbPeriod1H -> HOUR
                R.id.rbPeriod1D -> HOURS24
                R.id.rbPeriod1W -> WEEK
                R.id.rbPeriod1M -> MONTH
                R.id.rbPeriod1Y -> YEAR
                R.id.rbPeriodAll -> ALL
                else -> HOUR
            }
            findViewById<RadioButton>(id).setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
            selectedPeriod = period
            loadHistoricalData(period, fromCurrency, toCurrency)
        })
    }
}
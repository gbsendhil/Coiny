package com.binarybricks.coinhood.stories.sparkchart

import android.animation.ValueAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.HOUR
import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.Formatters
import com.binarybricks.coinhood.utils.chartAnimationDuration
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.historical_chart_module.view.*
import timber.log.Timber
import java.util.*


/**
 * Created by pairan on 1/10/18.
 * A compound layout to see historical charts.
 */


class HistoricalChartModule : ConstraintLayout {

    private lateinit var schedulerProvider: BaseSchedulerProvider

    private var historicalData: List<CryptoCompareHistoricalResponse.Data>? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val chatRepo by lazy {
        ChartRepository(schedulerProvider)
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

        loadHistoricalData(HOUR, fromCurrency, toCurrency)
        addChartScrubListener()
    }


    private fun loadHistoricalData(period: String, fromCurrency: String, toCurrency: String) {

        compositeDisposable.add(chatRepo.getCryptoHistoricalData(period, fromCurrency, toCurrency)
                .observeOn(schedulerProvider.ui())
                .subscribe({ datalist ->
                    if (datalist.isNotEmpty()) {
                        Timber.d("Data fetched with size ${datalist.size}")
                        historicalData = datalist
                        historicalChartView.adapter = HistoricalChartAdapter(datalist)
                    }
                }, {
                    Timber.e(it.localizedMessage)
                }))
    }


    private fun addChartScrubListener() {
        historicalChartView.setScrubListener { value ->
            if (value == null) {
                // reset the amount
            } else {
                val historicalData = value as CryptoCompareHistoricalResponse.Data
                tvChartPercentageChange.text = ""
                tvChartPeriod.text = formatter.formatDate(historicalData.time, 1000)
                animateCoinPrice(historicalData.close)
            }
        }
    }

    private fun animateCoinPrice(amount: String) {
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
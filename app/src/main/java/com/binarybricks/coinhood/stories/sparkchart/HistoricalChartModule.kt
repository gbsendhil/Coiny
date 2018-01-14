package com.binarybricks.coinhood.stories.sparkchart

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.HOUR
import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.formatAmount
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

    fun init(schedulerProvider: BaseSchedulerProvider, fromCurrency: String, toCurrency: String) {
        this.schedulerProvider = schedulerProvider
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.historical_chart_module, this)

        loadData(HOUR, fromCurrency, toCurrency)
    }

    private fun loadData(period: String, fromCurrency: String, toCurrency: String) {
        chatRepo.getCryptoHistoricalData(period, fromCurrency, toCurrency)
                .observeOn(schedulerProvider.ui())
                .subscribe({ datalist ->
                    if (datalist.isNotEmpty()) {
                        historicalData = datalist
                        val historicalChartAdapter = HistoricalChartAdapter(datalist)
                        historicalChartView.adapter = historicalChartAdapter
                    }
                }, {
                    Timber.e(it.localizedMessage)
                })

        historicalChartView.setScrubListener { value ->
            if (value == null) {
                // reset the amount
            } else {
                val historicalData = value as CryptoCompareHistoricalResponse.Data
                tvChartCoinPrice.text = formatAmount(historicalData.close)
            }
        }
    }

}
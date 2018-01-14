package com.binarybricks.coinhood.stories.sparkchart

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.HOUR
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import timber.log.Timber

/**
 * Created by pairan on 1/10/18.
 * A compound layout to see historical charts.
 */


class HistoricalChartModule : ConstraintLayout {

    private lateinit var schedulerProvider: BaseSchedulerProvider

    constructor(context: Context, schedulerProvider: BaseSchedulerProvider) : super(context) {
        this.schedulerProvider = schedulerProvider
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val chatRepo by lazy {
        ChartRepository(schedulerProvider)
    }

    fun init() {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.historical_chart_module, this)
    }

    private fun loadData(period: String) {
        chatRepo.getCryptoHistoricalData(HOUR, "BTC", "USD")
                .observeOn(schedulerProvider?.ui())
                .subscribe({

                }, {
                    Timber.e(it.localizedMessage)
                })
    }

}
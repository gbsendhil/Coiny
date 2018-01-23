package com.binarybricks.coinhood.stories

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.components.historicalchart.HistoricalChartModule
import com.binarybricks.coinhood.utils.ResourceProviderImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val schedulerProvider = SchedulerProvider.getInstance()
        val resourceProvider = ResourceProviderImpl(applicationContext)

        val historicalChartModule = HistoricalChartModule(schedulerProvider, resourceProvider, "BTC", "USD")

        lifecycle.addObserver(historicalChartModule)

        val historicalChartModuleView = historicalChartModule.init(this, null)

        llContainer.addView(historicalChartModuleView)

        historicalChartModule.loadData()
    }
}

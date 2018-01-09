package com.binarybricks.coinhood.stories

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.HOUR
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.stories.sparkchart.ChartRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chatRepo: ChartRepository = ChartRepository(SchedulerProvider.getInstance())

        chatRepo.getCryptoHistoricalData(HOUR, "BTC", "USD")
    }
}

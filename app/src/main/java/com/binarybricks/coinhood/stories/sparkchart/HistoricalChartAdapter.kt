package com.binarybricks.coinhood.stories.sparkchart

import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.robinhood.spark.SparkAdapter

/**
 * Created by pairan on 1/13/18.
 */

class HistoricalChartAdapter(private val historicalData: List<CryptoCompareHistoricalResponse.Data>) : SparkAdapter() {

    override fun getY(index: Int): Float {
        return historicalData[index].close.toFloat()
    }

    override fun getItem(index: Int): CryptoCompareHistoricalResponse.Data {
        return historicalData[index]
    }

    override fun getCount(): Int {
        return historicalData.size
    }

}
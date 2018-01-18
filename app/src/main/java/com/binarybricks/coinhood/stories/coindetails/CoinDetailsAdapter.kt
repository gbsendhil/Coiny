package com.binarybricks.coinhood.stories.coindetails

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.stories.sparkchart.HistoricalChartModule
import com.binarybricks.coinhood.utils.ResourceProvider


/**
 * Created by pranay airan on 1/18/18.
 */

class CoinDetailsAdapter(private val context: Context,
                         private val coinDetailList: List<Any>,
                         private val schedulerProvider: BaseSchedulerProvider,
                         private val resourceProvider: ResourceProvider) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HISTORICAL_CHART = 0
    private val BUY_SELL = 1

    override fun getItemViewType(position: Int): Int {
//        if (items.get(position) instanceof User) {
//            return USER
//        } else if (items.get(position) instanceof String) {
//            return IMAGE
//        }
//        return -1
        return HISTORICAL_CHART
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder

        viewHolder = when (viewType) {
            HISTORICAL_CHART -> {
                val historicalChartModule = HistoricalChartModule(schedulerProvider, resourceProvider, "BTC", "USD")
                val historicalChartModuleView = historicalChartModule.init(context)
                HistoricalChartViewHolder(historicalChartModuleView)
            }
            else -> {
                HistoricalChartViewHolder(TextView(context))
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            HISTORICAL_CHART -> {
                // ideally here we pass a content to the view if there is anything in chart case
                // we can pass the other data to it that is loaded from outside
            }
        }
    }

    override fun getItemCount(): Int {
        return coinDetailList.size
    }

    class HistoricalChartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
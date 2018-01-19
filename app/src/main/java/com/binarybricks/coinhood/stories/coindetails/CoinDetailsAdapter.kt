package com.binarybricks.coinhood.stories.coindetails

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.AboutCoinModule
import com.binarybricks.coinhood.components.sparkchart.HistoricalChartModule
import com.binarybricks.coinhood.network.models.Coin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProvider


/**
 * Created by pranay airan on 1/18/18.
 */

class CoinDetailsAdapter(private val context: Context,
                         private val coinDetailList: List<Any>,
                         private val schedulerProvider: BaseSchedulerProvider,
                         private val resourceProvider: ResourceProvider) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HISTORICAL_CHART = 0
    private val ABOUT_COIN = 1

    override fun getItemViewType(position: Int): Int {
        if (coinDetailList[position] is HistoricalChartModule.HistoricalChartModuleData) {
            return HISTORICAL_CHART
        } else if (coinDetailList[position] is AboutCoinModule.AboutCoinModuleData) {
            return ABOUT_COIN
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder

        viewHolder = when (viewType) {
            HISTORICAL_CHART -> {
                val historicalChartModule = HistoricalChartModule(schedulerProvider, resourceProvider, "BTC", "USD")
                val historicalChartModuleView = historicalChartModule.init(context, parent)
                HistoricalChartViewHolder(historicalChartModuleView, historicalChartModule)
            }
            ABOUT_COIN -> {
                val aboutCoinModule = AboutCoinModule()
                val aboutCardModuleView = aboutCoinModule.init(context, parent)
                AboutCoinViewHolder(aboutCardModuleView, aboutCoinModule)
            }
            else -> {
                throw IllegalAccessException("Undefined view type")
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            HISTORICAL_CHART -> {
                // load data
                val historicalChartViewHolder = viewHolder as HistoricalChartViewHolder
                val historicalChartModuleData = coinDetailList[position] as HistoricalChartModule.HistoricalChartModuleData

                historicalChartViewHolder.loadHistoricalChartData()
                historicalChartViewHolder.addCoinAndAnimateCoinPrice(historicalChartModuleData.coinWithCurrentPrice)
            }
            ABOUT_COIN -> {
                val aboutCoinViewHolder = viewHolder as AboutCoinViewHolder
                aboutCoinViewHolder.showAboutCoinText((coinDetailList[position] as AboutCoinModule.AboutCoinModuleData).aboutcoin)
            }
        }
    }

    override fun getItemCount(): Int {
        return coinDetailList.size
    }

    class HistoricalChartViewHolder(itemView: View, private val historicalChartModule: HistoricalChartModule) : RecyclerView.ViewHolder(itemView) {
        fun loadHistoricalChartData() {
            historicalChartModule.loadData()
        }

        fun addCoinAndAnimateCoinPrice(coin: Coin?) {
            historicalChartModule.addCoinAndAnimateCoinPrice(coin)
        }
    }

    class AboutCoinViewHolder(itemView: View, private val aboutCoinModule: AboutCoinModule) : RecyclerView.ViewHolder(itemView) {

        fun showAboutCoinText(aboutCoin: String) {
            aboutCoinModule.showAboutCoinText(aboutCoin)
        }
    }
}
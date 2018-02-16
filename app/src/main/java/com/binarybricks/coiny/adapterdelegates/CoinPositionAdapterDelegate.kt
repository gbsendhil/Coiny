package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.AboutCoinModule
import com.binarybricks.coiny.components.CoinPositionCard
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate

/**
 * Created by Pranay Airan
 */

class CoinPositionAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is CoinPositionCard.CoinPositionCardModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val coinPositionCard = CoinPositionCard()
        val coinPositionCardView = coinPositionCard.init(parent.context, parent)
        return CoinPositionCardViewHolder(coinPositionCardView, coinPositionCard)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val aboutCoinViewHolder = holder as CoinPositionCardViewHolder
        aboutCoinViewHolder.showAboutCoinText((items[position] as AboutCoinModule.AboutCoinModuleData).aboutcoin)
    }

    class CoinPositionCardViewHolder(itemView: View, private val coinPositionCard: CoinPositionCard) : RecyclerView.ViewHolder(itemView) {
        fun showAboutCoinText(coins: String) {
            coinPositionCard.showNoOfCoinsView(coins)
        }
    }
}
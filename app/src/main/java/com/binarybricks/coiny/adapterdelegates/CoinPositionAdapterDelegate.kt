package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.CoinPositionCard
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by Pranay Airan
 */

class CoinPositionAdapterDelegate(private val resourceProvider: ResourceProvider) : AdapterDelegate<List<ModuleItem>>() {
    private val coinPositionCard by lazy {
        CoinPositionCard(resourceProvider)
    }

    override fun isForViewType(items: List<ModuleItem>, position: Int): Boolean {
        return items[position] is CoinPositionCard.CoinPositionCardModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val coinPositionCardView = coinPositionCard.init(LayoutInflater.from(parent.context), parent)
        return CoinPositionCardViewHolder(coinPositionCardView, coinPositionCard)
    }

    override fun onBindViewHolder(items: List<ModuleItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val aboutCoinViewHolder = holder as CoinPositionCardViewHolder
        aboutCoinViewHolder.showAboutCoinText((items[position] as CoinPositionCard.CoinPositionCardModuleData))
    }

    class CoinPositionCardViewHolder(override val containerView: View, private val coinPositionCard: CoinPositionCard)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun showAboutCoinText(coinPositionCardModuleData: CoinPositionCard.CoinPositionCardModuleData) {
            coinPositionCard.showNoOfCoinsView(coinPositionCardModuleData)
        }
    }
}
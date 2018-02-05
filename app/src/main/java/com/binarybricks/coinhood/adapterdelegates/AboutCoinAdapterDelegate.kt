package com.binarybricks.coinhood.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.components.AboutCoinModule
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by pranay airan on 1/23/18.
 */

class AboutCoinAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is AboutCoinModule.AboutCoinModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val aboutCoinModule = AboutCoinModule()
        val aboutCardModuleView = aboutCoinModule.init(LayoutInflater.from(parent.context), parent)
        return AboutCoinViewHolder(aboutCardModuleView, aboutCoinModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val aboutCoinViewHolder = holder as AboutCoinViewHolder
        aboutCoinViewHolder.showAboutCoinText((items[position] as AboutCoinModule.AboutCoinModuleData).aboutcoin)
    }

    class AboutCoinViewHolder(itemView: View, private val aboutCoinModule: AboutCoinModule) : RecyclerView.ViewHolder(itemView) {
        fun showAboutCoinText(aboutCoin: String) {
            aboutCoinModule.showAboutCoinText(itemView, aboutCoin)
        }
    }
}
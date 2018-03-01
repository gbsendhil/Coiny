package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.AddCoinModule
import com.binarybricks.coiny.data.database.entities.Coin
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate


/**
 * Created by Pragya Agrawal
 */

class AddCoinAdapterDelegate : AdapterDelegate<List<Any>>() {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is AddCoinModule.AddCoinModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val addCoinModule = AddCoinModule()
        val addCoinModuleView = addCoinModule.init(LayoutInflater.from(parent.context), parent)
        return AddCoinViewHolder(addCoinModuleView, addCoinModule)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        val addCoinViewHolder = holder as AddCoinAdapterDelegate.AddCoinViewHolder
        addCoinViewHolder.addCoinListner((items[position] as AddCoinModule.AddCoinModuleData).coin)
    }

    class AddCoinViewHolder(itemView: View, private val addCoinModule: AddCoinModule) : RecyclerView.ViewHolder(itemView) {
        fun addCoinListner(coin: Coin) {
            addCoinModule.addCoinListner(itemView, coin)
        }
    }
}
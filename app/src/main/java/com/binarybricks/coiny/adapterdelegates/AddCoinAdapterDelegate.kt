package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.AddCoinModule
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
        return AddCoinViewHolder(addCoinModuleView)
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: List<Any>) {

    }

    class AddCoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
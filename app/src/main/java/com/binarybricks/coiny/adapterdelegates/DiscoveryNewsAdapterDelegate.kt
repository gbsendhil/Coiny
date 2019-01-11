package com.binarybricks.coiny.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.components.DiscoveryNewsModule
import com.binarybricks.coiny.components.ModuleItem
import com.binarybricks.coiny.utils.ResourceProvider
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by Pranay Airan
 */

class DiscoveryNewsAdapterDelegate(private val resourceProvider: ResourceProvider) : AdapterDelegate<List<ModuleItem>>() {

    private val discoveryNewsModule by lazy {
        DiscoveryNewsModule(resourceProvider)
    }

    override fun isForViewType(items: List<ModuleItem>, position: Int): Boolean {
        return items[position] is DiscoveryNewsModule.DiscoveryNewsModuleData
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val discoveryNewsModuleView = discoveryNewsModule.init(LayoutInflater.from(parent.context), parent)
        return DiscoveryNewsHolder(discoveryNewsModuleView, discoveryNewsModule)
    }

    override fun onBindViewHolder(items: List<ModuleItem>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        val discoveryNewsHolder = holder as DiscoveryNewsHolder
        discoveryNewsHolder.showNewsOnDiscoverFeed((items[position] as DiscoveryNewsModule.DiscoveryNewsModuleData))
    }

    class DiscoveryNewsHolder(override val containerView: View, private val discoveryNewsModule: DiscoveryNewsModule)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun showNewsOnDiscoverFeed(discoveryNewsModuleData: DiscoveryNewsModule.DiscoveryNewsModuleData) {
            discoveryNewsModule.showNewsOnDiscoverFeed(containerView, discoveryNewsModuleData)
        }
    }
}
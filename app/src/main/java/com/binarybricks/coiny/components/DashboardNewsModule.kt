package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.network.models.CryptoCompareNews
import com.binarybricks.coiny.utils.openCustomTab
import kotlinx.android.synthetic.main.dashboard_news_module.view.*
import timber.log.Timber

/**
 * Created by Pranay Airan
 *
 * Simple class that wraps all logic related to showing news on dashboard.
 */

class DashboardNewsModule : Module() {

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dashboard_news_module, parent, false)
    }

    fun showNewsOnDashboard(inflatedView: View, dashboardNewsModuleData: DashboardNewsModuleData) {
        var i = 0

        if (!dashboardNewsModuleData.coinNews.isNullOrEmpty()) {
            inflatedView.pbLoading.visibility = View.GONE
            inflatedView.tvNewsTitle.text = dashboardNewsModuleData.coinNews[i].title
            inflatedView.clNewsArticleContainer.setOnClickListener {
                dashboardNewsModuleData.coinNews[i].url?.let {
                    openCustomTab(it, inflatedView.context)
                    CoinyCache.updateCryptoCompareNews(dashboardNewsModuleData.coinNews[i])
                    if (i < dashboardNewsModuleData.coinNews.size) {
                        i++
                        inflatedView.tvNewsTitle.text = dashboardNewsModuleData.coinNews[i].title
                    }
                }
            }
        }
    }

    override fun cleanUp() {
        Timber.d("Clean up about DashboardNews module")
    }

    data class DashboardNewsModuleData(val coinNews: List<CryptoCompareNews>?) : ModuleItem
}
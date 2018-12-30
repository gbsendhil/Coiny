package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.utils.openCustomTab
import kotlinx.android.synthetic.main.coin_about_module.view.*
import timber.log.Timber

/**
 * Created by Pranay Airan
 *
 * Simple class that wraps all logic related to showing about us section
 */

class AboutCoinModule : Module() {

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_about_module, parent, false)
    }

    fun showAboutCoinText(inflatedView: View, aboutCoinModuleData: AboutCoinModuleData) {

        inflatedView.tvAboutCoin.text = getCleanedUpDescription(aboutCoinModuleData.coin.description) ?: inflatedView.context.getString(R.string.info_unavilable)

        aboutCoinModuleData.coin.website?.let { url ->
            inflatedView.tvWebsiteValue.text = getCleanUrl(url)
            inflatedView.tvWebsiteValue.setOnClickListener {
                openCustomTab(url, inflatedView.context)
            }
        }

        aboutCoinModuleData.coin.twitter?.let { url ->
            inflatedView.tvTwitterValue.text = inflatedView.context.getString(R.string.twitterValue, aboutCoinModuleData.coin.twitter)
            inflatedView.tvTwitterValue.setOnClickListener {
                openCustomTab(inflatedView.context.getString(R.string.twitterUrl, aboutCoinModuleData.coin.twitter)
                        ?: "", inflatedView.context)
            }
        }

        aboutCoinModuleData.coin.reddit?.let { url ->
            inflatedView.tvRedditValue.text = getCleanUrl(url)
            inflatedView.tvRedditValue.setOnClickListener {
                openCustomTab(url, inflatedView.context)
            }
        }

        aboutCoinModuleData.coin.github?.let { url ->
            inflatedView.tvGithubValue.text = getCleanUrl(url)
            inflatedView.tvGithubValue.setOnClickListener {
                openCustomTab(url, inflatedView.context)
            }
        }

        inflatedView.tvAboutCoin.setOnClickListener {
            inflatedView.tvAboutCoin.maxLines = Int.MAX_VALUE
        }
    }

    override fun cleanUp() {
        Timber.d("Clean up about coinSymbol module")
    }

    data class AboutCoinModuleData(val coin: Coin) : ModuleItem

    private fun getCleanedUpDescription(description: String?): String? {
        if (description != null) {
            return description.replace(Regex("\\<.*?>"), "")
        }
        return description
    }

    private fun getCleanUrl(url: String): String {
        return url.replace("http://", "").replace("https://", "")
    }
}
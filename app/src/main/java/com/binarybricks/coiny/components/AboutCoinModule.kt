package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
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

        inflatedView.tvAboutCoin.text = aboutCoinModuleData.aboutCoin ?: inflatedView.context.getString(R.string.info_unavilable)
//        inflatedView.cvAboutCoin.setOnClickListener {
//            inflatedView.tvAboutCoin.maxLines = Int.MAX_VALUE
//        }
    }

    override fun cleanUp() {
        Timber.d("Clean up about coinSymbol module")
    }

    data class AboutCoinModuleData(val aboutCoin: String?) : ModuleItem
}
package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.coin_about_module.view.*

/**
 * Created by Pranay Airan
 *
 * Simple class that wraps all logic related to showing about us section
 */

class AboutCoinModule {

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_about_module, parent, false)
    }

    fun showAboutCoinText(inflatedView: View, coinText: String) {
        inflatedView.tvAboutCoin.text = coinText
    }

    data class AboutCoinModuleData(val aboutcoin: String)
}
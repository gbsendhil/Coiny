package com.binarybricks.coinhood.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import kotlinx.android.synthetic.main.about_coin_module.view.*

/**
 * Created by pranay airan on 1/19/18.
 */

class AboutCoinModule {

    private lateinit var inflatedView: View

    fun init(context: Context, parent: ViewGroup?): View {

        val layoutInflater = LayoutInflater.from(context)
        inflatedView = layoutInflater.inflate(R.layout.about_coin_module, parent, false)
        return inflatedView
    }

    fun showAboutCoinText(coinText: String) {
        inflatedView.tvAboutCoin.text = coinText
    }

    data class AboutCoinModuleData(val aboutcoin: String)
}
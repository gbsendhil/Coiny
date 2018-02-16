package com.binarybricks.coiny.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.coin_position_card_module.view.*

/**
 * Created by Pragya Agrawal
 */

class CoinPositionCard {

    private lateinit var inflatedView: View

    fun init(context: Context, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        inflatedView = layoutInflater.inflate(R.layout.coin_position_card_module, parent, false)

        return inflatedView
    }

    fun showNoOfCoinsView(coins: String) {
        inflatedView.noOfCoins.text = coins
    }

    data class CoinPositionCardModuleData(val noOfCoins: String)
}
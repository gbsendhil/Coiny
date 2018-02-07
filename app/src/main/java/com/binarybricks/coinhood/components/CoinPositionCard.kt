package com.binarybricks.coinhood.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import kotlinx.android.synthetic.main.coin_position_card_component.view.*

/**
 * @author Pragya Agrawal on January 13, 2018
 */

class CoinPositionCard {

    private lateinit var inflatedView: View

    fun init(context: Context, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        inflatedView = layoutInflater.inflate(R.layout.coin_position_card_component, parent, false)

        return inflatedView
    }

    fun showNoOfCoinsView(coins: String) {
        inflatedView.noOfCoins.text = coins
    }

    data class CoinPositionCardModuleData(val noOfCoins: String)
}
package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import kotlinx.android.synthetic.main.coin_position_card_module.view.*
import timber.log.Timber

/**
 * Created by Pragya Agrawal
 */

class CoinPositionCard : Module() {

    private lateinit var inflatedView: View

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {

        inflatedView = layoutInflater.inflate(R.layout.coin_position_card_module, parent, false)
        return inflatedView
    }

    fun showNoOfCoinsView(coinPositionCardModuleData: CoinPositionCardModuleData) {
        inflatedView.noOfCoins.text = coinPositionCardModuleData.noOfCoins
    }

    override fun cleanUp() {
        Timber.d("Clean up add coin module")
    }

    data class CoinPositionCardModuleData(val noOfCoins: String) : ModuleItem
}
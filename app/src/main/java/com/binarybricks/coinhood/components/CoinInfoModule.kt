package com.binarybricks.coinhood.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.utils.defaultExchange
import kotlinx.android.synthetic.main.coin_info_module.view.*

/**
 * @author Pragya Agrawal on February 1, 2018
 *
 * Simple class that wraps all logic related to Coin info
 */

class CoinInfoModule {

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_info_module, parent, false)
    }

    fun showCoinInfo(inflatedView: View, coinInfoModuleData: CoinInfoModuleData) {

        var exchange = coinInfoModuleData.exchange
        if (exchange.equals(defaultExchange, true)) {
            exchange = inflatedView.context.getString(R.string.global_avg)
        }

        inflatedView.tvExchangeName.text = exchange
        inflatedView.tvAlgorithmName.text = coinInfoModuleData.algorithm
        inflatedView.tvProofTypeName.text = coinInfoModuleData.proofType
    }

    data class CoinInfoModuleData(val exchange: String, val algorithm: String, val proofType: String)
}
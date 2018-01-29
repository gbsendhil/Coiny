package com.binarybricks.coinhood.utils

import android.content.Context
import com.binarybricks.coinhood.R

/**
 * Created by pranay airan on 1/15/18.
 */


fun getAboutStringForCoin(coinSymbol: String, context: Context): String {
    return when (coinSymbol.toUpperCase()) {
        "BTC" -> context.getString(R.string.btc)
        "ETH" -> context.getString(R.string.eth)
        "LTC" -> context.getString(R.string.ltc)
        "XRP" -> context.getString(R.string.xrp)

        else -> context.getString(R.string.unavailable)
    }
}
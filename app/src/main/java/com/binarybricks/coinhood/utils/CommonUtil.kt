package com.binarybricks.coinhood.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import com.binarybricks.coinhood.R


/**
Created by Pranay Airan 1/15/18.
 */


fun getAboutStringForCoin(coinSymbol: String, context: Context?): String {
    if (context != null) {
        return when (coinSymbol.toUpperCase()) {
            "BTC" -> context.getString(R.string.btc)
            "ETH" -> context.getString(R.string.eth)
            "LTC" -> context.getString(R.string.ltc)
            "XRP" -> context.getString(R.string.xrp)

            else -> context.getString(R.string.unavailable)
        }
    }

    return "N/A"
}

/**
 * Get's the browser intent to open a webpage.
 */
fun getBrowserIntent(url: String): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    return intent
}

fun dpToPx(context: Context, dp: Int): Int {
    val r = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).toInt()
}
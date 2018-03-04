package com.binarybricks.coiny.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import com.binarybricks.coiny.R


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

/**
 * Open the URL in chrome custom tab
 */
fun openCustomTab(url: String, context: Context) {

    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
    builder.set
    // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
    val customTabsIntent = builder.build()
    // and launch the desired Url with CustomTabsIntent.launchUrl()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

fun dpToPx(context: Context?, dp: Int): Int {

    if (context != null) {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).toInt()
    }
    return dp
}

fun getDefaultExchangeText(exchangeName: String, context: Context): String {
    if (exchangeName.equals(defaultExchange, true)) {
        return context.getString(R.string.global_avg)
    }

    return exchangeName
}

fun dismissKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val focusedView = activity.currentFocus
    if (focusedView != null) {
        imm?.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }
}
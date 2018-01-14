package com.binarybricks.coinhood.utils

import java.text.NumberFormat
import java.util.*

/**
 * Created by pairan on 1/13/18.
 */

/**
 * Use to format amount that we get it from api
 */
fun formatAmount(number: String, currency: Currency = Currency.getInstance("USD")): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.currency = currency
    return formatter.format(number.toDouble())
}
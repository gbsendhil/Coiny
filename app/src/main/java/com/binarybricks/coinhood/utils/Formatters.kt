package com.binarybricks.coinhood.utils

import android.text.format.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by pranay airan on 1/13/18.
 */

/**
 * Use to format amount that we get it from api
 */

class Formatters {

    private val formatter: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }

    private val calendar: Calendar by lazy {
        Calendar.getInstance(Locale.getDefault())
    }

    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM/YYYY hh:mm aaa"), Locale.getDefault())
    }

    fun formatAmount(amount: String, currency: Currency = Currency.getInstance("USD")): String {
        formatter.currency = currency
        return formatter.format(amount.toDouble())
    }

    fun formatDate(timestamp: String, multiplier: Int): String {
        calendar.timeInMillis = timestamp.toLong() * multiplier // time we get from some api call is in seconds
        return simpleDateFormat.format(calendar.time)
    }
}
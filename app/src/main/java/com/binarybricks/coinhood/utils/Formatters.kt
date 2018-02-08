package com.binarybricks.coinhood.utils

import android.text.format.DateFormat
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
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

    private val million = BigDecimal(1000000)
    private val thousand = BigDecimal(1000)
    private val mathContext = MathContext(0, RoundingMode.HALF_UP)

    private val formatter: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale.getDefault())
    }

    private val formatterNumber: NumberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.getDefault())
    }

    private val calendar: Calendar by lazy {
        Calendar.getInstance(Locale.getDefault())
    }

    private val simpleDateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM/YYYY hh:mm aaa"), Locale.getDefault())
    }

    fun formatAmount(amount: String, currency: Currency = Currency.getInstance("USD"), rounding: Boolean = false): String {
        formatter.currency = currency

        val amountNumber = BigDecimal(amount)

        if (rounding && amountNumber > BigDecimal.ONE) {
            formatter.maximumFractionDigits = 0
        }

        return if (amountNumber < million) {
            formatter.format(amountNumber)
        } else {
            val remainder = amountNumber.divide(million, mathContext) // divide this number by million
            if (remainder <= thousand) {
                "${formatter.format(remainder.toInt())} Million"
            } else {
                "${formatter.format(remainder.divide(thousand, mathContext).toInt())} Billion"
            }
        }
    }

    fun formatNumber(number: Int): String? {
        return formatterNumber.format(number)
    }

    fun formatDate(timestamp: String, multiplier: Int): String {
        calendar.timeInMillis = timestamp.toLong() * multiplier // time we get from some api call is in seconds
        return simpleDateFormat.format(calendar.time)
    }

}
package com.binarybricks.coiny.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class CurrencyUtils {

    companion object {

        private val kDecimalFormatter by lazy {
            DecimalFormat("\u00a4###,### K;-\u00a4###,### K")
        }

        private val mDecimalFormatter by lazy {
            DecimalFormat("\u00a4###,### M;-\u00a4###,### M")
        }

        private val bDecimalFormatter by lazy {
            DecimalFormat("\u00a4###,### B;-\u00a4###,### B")
        }

        private val decimalFormatter by lazy {
            DecimalFormat("\u00a4###,###.##;-\u00a4###,###.##")
        }

        /**
         * Formats a value to get natural text
         * amounts > 1 million are displayed as M millions with one decimal (e.g 1,300,000 -> 1.3M)
         * amounts > 1 thousand are displayed as K thousands with one decimal (e.g 1,300 -> 1.3K)
         * amounts > 100 are displayed with no decimal places
         * amounts between 0.01 and less than 100 have decimal places
         * a zero amount will have no decimal places shown
         * Any K or M amounts that have 0 as the decimal are shown as whole values (e.g. 1.0M - > 1M)
         *
         * @param rawValue total value to be formatted for display
         * @return formatted value as string
         */
        fun getNaturalTextForDisplay(rawValue: BigDecimal, currency: Currency): String {
            val df: DecimalFormat
            val displayProfit: BigDecimal
            val profitToCompare = rawValue.abs()

            when {
                profitToCompare.compareTo(1000000000.toBigDecimal()) > -1 -> {
                    df = bDecimalFormatter
                    df.roundingMode = RoundingMode.HALF_UP
                    df.currency = currency
                    displayProfit = rawValue.divide(1000000000.toBigDecimal(), 1, BigDecimal.ROUND_HALF_UP)
                    if (displayProfit.remainder(BigDecimal.ONE).abs().compareTo(0.1.toBigDecimal()) == -1) df.maximumFractionDigits = 0 else df.maximumFractionDigits = 1
                }
                profitToCompare.compareTo(1000000.toBigDecimal()) > -1 -> {
                    df = mDecimalFormatter
                    df.roundingMode = RoundingMode.HALF_UP
                    df.currency = currency
                    displayProfit = rawValue.divide(1000000.toBigDecimal(), 1, BigDecimal.ROUND_HALF_UP)
                    if (displayProfit.remainder(BigDecimal.ONE).abs().compareTo(0.1.toBigDecimal()) == -1) df.maximumFractionDigits = 0 else df.maximumFractionDigits = 1
                }

                profitToCompare.compareTo(1000.toBigDecimal()) > -1 -> {
                    df = kDecimalFormatter
                    df.roundingMode = RoundingMode.HALF_UP
                    df.currency = currency
                    displayProfit = rawValue.divide(1000.toBigDecimal(), 1, BigDecimal.ROUND_HALF_UP)
                    if (displayProfit.remainder(BigDecimal.ONE).abs().compareTo(0.1.toBigDecimal()) == -1) df.maximumFractionDigits = 0 else df.maximumFractionDigits = 1
                }

                else -> {
                    df = decimalFormatter
                    df.currency = currency
                    displayProfit = rawValue
                }
            }

            return df.format(displayProfit)
        }
    }
}
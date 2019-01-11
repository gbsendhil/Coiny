package com.binarybricks.coiny.utils

import com.mynameismidori.currencypicker.ExtendedCurrency
import com.mynameismidori.currencypicker.R

class CoinyExtendedCurrency {

    companion object {
        val CURRENCIES = listOf(
                ExtendedCurrency("EUR", "Euro", "€", R.drawable.flag_eur),
                ExtendedCurrency("USD", "United States Dollar", "$", R.drawable.flag_usd),
                ExtendedCurrency("GBP", "British Pound", "£", R.drawable.flag_gbp),
                ExtendedCurrency("CZK", "Czech Koruna", "Kč", R.drawable.flag_czk),
                ExtendedCurrency("TRY", "Turkish Lira", "₺", R.drawable.flag_try),
                ExtendedCurrency("AED", "Emirati Dirham", "د.إ", R.drawable.flag_aed),
                ExtendedCurrency("AUD", "Australian Dollar", "$", R.drawable.flag_aud),
                ExtendedCurrency("BRL", "Brazil Real", "R$", R.drawable.flag_brl),
                ExtendedCurrency("CAD", "Canada Dollar", "$", R.drawable.flag_cad),
                ExtendedCurrency("CHF", "Switzerland Franc", "CHF", R.drawable.flag_chf),
                ExtendedCurrency("CNY", "China Yuan Renminbi", "¥", R.drawable.flag_cny),
                ExtendedCurrency("DKK", "Denmark Krone", "kr", R.drawable.flag_dkk),
                ExtendedCurrency("GHS", "Ghana Cedi", "¢", R.drawable.flag_ghs),
                ExtendedCurrency("HKD", "Hong Kong Dollar", "$", R.drawable.flag_hkd),
                ExtendedCurrency("IDR", "Indonesia Rupiah", "Rp", R.drawable.flag_idr),
                ExtendedCurrency("ILS", "Israel Shekel", "₪", R.drawable.flag_ils),
                ExtendedCurrency("JPY", "Japanese Yen", "¥", R.drawable.flag_jpy),
                ExtendedCurrency("KES", "Kenyan Shilling", "KSh", R.drawable.flag_kes),
                ExtendedCurrency("KRW", "Korea (South) Won", "₩", R.drawable.flag_krw),
                ExtendedCurrency("MXN", "Mexico Peso", "$", R.drawable.flag_mxn),
                ExtendedCurrency("MYR", "Malaysia Ringgit", "RM", R.drawable.flag_myr),
                ExtendedCurrency("NGN", "Nigeria Naira", "₦", R.drawable.flag_ngn),
                ExtendedCurrency("NOK", "Norway Krone", "kr", R.drawable.flag_nok),
                ExtendedCurrency("PKR", "Pakistan Rupee", "₨", R.drawable.flag_pkr),
                ExtendedCurrency("PLN", "Poland Zloty", "zł", R.drawable.flag_pln),
                ExtendedCurrency("RUB", "Russia Ruble", "₽", R.drawable.flag_rub),
                ExtendedCurrency("SEK", "Sweden Krona", "kr", R.drawable.flag_sek),
                ExtendedCurrency("SGD", "Singapore Dollar", "$", R.drawable.flag_sgd),
                ExtendedCurrency("THB", "Thailand Baht", "฿", R.drawable.flag_thb),
                ExtendedCurrency("UAH", "Ukraine Hryvnia", "₴", R.drawable.flag_uah),
                ExtendedCurrency("ZAR", "South Africa Rand", "R", R.drawable.flag_zar)
        )
    }
}
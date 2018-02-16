package com.binarybricks.coiny.utils

import com.binarybricks.coiny.network.DATA
import com.binarybricks.coiny.network.RAW
import com.binarybricks.coiny.network.models.CCCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*

/**
 Created by Pranay Airan 1/15/18.
 */
fun getCoinPricesFromJson(jsonObject: JsonObject): ArrayList<CoinPrice> {
    val coinPriceList: ArrayList<CoinPrice> = ArrayList()

    if (jsonObject.has(RAW)) {
        val rawCoinObject = jsonObject.getAsJsonObject(RAW)
        val coins = rawCoinObject.keySet() // this will give us list of all the coins in raw like BTC, ETH
        coins.forEach { coinName ->
            val toCurrencies = rawCoinObject.getAsJsonObject(coinName) // this will give us list of prices for this coin in currencies we asked for
            toCurrencies.keySet().forEach {
                val coinJsonObject = toCurrencies.getAsJsonObject(it) // this will give us the price object we need
                val coin = Gson().fromJson(coinJsonObject, CoinPrice::class.java)
                coinPriceList.add(coin)
            }
        }
    }

    return coinPriceList
}

fun getCoinsFromJson(jsonObject: JsonObject): ArrayList<CCCoin> {
    val CCCoinList: ArrayList<CCCoin> = ArrayList()

    if (jsonObject.has(DATA)) {
        val rawCoinObject = jsonObject.getAsJsonObject(DATA)
        val coins = rawCoinObject.keySet() // this will give us list of all the coins in DATA like BTC, ETH
        coins.forEach { coinName ->
            val toCurrencies = rawCoinObject.getAsJsonObject(coinName)
            val coin = Gson().fromJson(toCurrencies, CCCoin::class.java)
            CCCoinList.add(coin)
        }
    }

    return CCCoinList
}

fun getExchangeListFromJson(jsonObject: JsonObject): ArrayList<String> {
    val exchangeList: ArrayList<String> = ArrayList()

    val exchanges = jsonObject.keySet() // this will give us list of all the exchanges like Coinbase, Binance etc
    exchanges.forEach { exchangeName ->
        exchangeList.add(exchangeName)
        //jsonObject.getAsJsonObject("exchangeName").keySet().toList().
    }

    return exchangeList
}
package com.binarybricks.coiny.utils

import com.binarybricks.coiny.network.DATA
import com.binarybricks.coiny.network.RAW
import com.binarybricks.coiny.network.models.CCCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.models.ExchangePair
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal


/**
Created by Pranay Airan 1/15/18.
 */

fun getCoinPriceFromJson(jsonObject: JsonObject): BigDecimal {
    var coinPrice = BigDecimal.ZERO

    val prices = jsonObject.keySet() // this will give us list of all the currency like USD, EUR
    prices.forEach { currency ->
        coinPrice = jsonObject.getAsJsonPrimitive(currency).asBigDecimal
    }

    return coinPrice
}

fun getCoinPricesFromJson(jsonObject: JsonObject): ArrayList<CoinPrice> {
    val coinPriceList: ArrayList<CoinPrice> = ArrayList()

    if (jsonObject.has(RAW)) {
        val rawCoinObject = jsonObject.getAsJsonObject(RAW)
        val coins = rawCoinObject.keySet() // this will give us list of all the coins in raw like BTC, ETH
        coins.forEach { coinName ->
            val toCurrencies = rawCoinObject.getAsJsonObject(coinName) // this will give us list of prices for this coinSymbol in currencies we asked for
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

fun getExchangeListFromJson(jsonObject: JsonObject): HashMap<String, MutableList<ExchangePair>> {
    val coinExchangeSet = HashMap<String, MutableList<ExchangePair>>()
    val gson = Gson()
    val listType = object : TypeToken<List<String>>() {
    }.type

    val exchangeSet = jsonObject.entrySet().iterator()
    var exchangePairList: MutableList<ExchangePair>
    exchangeSet.forEach { exchange ->
        val coinSet = exchange.value.asJsonObject.entrySet().iterator()
        coinSet.forEach { coin ->
            exchangePairList = mutableListOf()
            if (coinExchangeSet.containsKey(coin.key)) {
                exchangePairList = coinExchangeSet[coin.key] ?: mutableListOf()
            }

            exchangePairList.add(ExchangePair(exchange.key, gson.fromJson(coin.value.asJsonArray, listType)))

            coinExchangeSet[coin.key] = exchangePairList
        }
    }

    return coinExchangeSet
}
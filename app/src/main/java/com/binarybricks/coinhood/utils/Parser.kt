package com.binarybricks.coinhood.utils

import com.binarybricks.coinhood.network.RAW
import com.binarybricks.coinhood.network.models.Coin
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*

/**
 * Created by pranay airan on 1/15/18.
 */
fun getCoinsFromJson(jsonObject: JsonObject): ArrayList<Coin> {
    val coinList: ArrayList<Coin> = ArrayList()

    if (jsonObject.has(RAW)) {
        val rawCoinObject = jsonObject.getAsJsonObject(RAW)
        val coins = rawCoinObject.keySet() // this will give us list of all the coins in raw like BTC, ETH
        coins.forEach { coinName ->
            val toCurrencies = rawCoinObject.getAsJsonObject(coinName) // this will give us list of prices for this coin in currencies we asked for
            toCurrencies.keySet().forEach {
                val coinJsonObject = toCurrencies.getAsJsonObject(it) // this will give us the price object we need
                val coin = Gson().fromJson(coinJsonObject, Coin::class.java)
                coinList.add(coin)
            }
        }
    }

    return coinList
}
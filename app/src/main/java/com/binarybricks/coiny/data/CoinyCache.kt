package com.binarybricks.coiny.data

import com.binarybricks.coiny.network.models.CCCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.models.CryptoPanicNews
import com.binarybricks.coiny.network.models.ExchangePair

/**
 * Created by Pragya Agrawal
 *
 * In memory cache for certain objects that we want to cache only for the app session
 */

object CoinyCache {

    // cache the news since we don't want to overload the server. 
    var newsMap: MutableMap<String, CryptoPanicNews> = hashMapOf()

    var coinPriceMap: HashMap<String, CoinPrice> = hashMapOf()

    var coinExchangeMap: HashMap<String, MutableList<ExchangePair>> = hashMapOf()

    var coinList: ArrayList<CCCoin> = ArrayList()
}
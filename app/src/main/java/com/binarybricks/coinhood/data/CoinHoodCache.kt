package com.binarybricks.coinhood.data

import com.binarybricks.coinhood.network.models.CryptoPanicNews

/**
 * Created by Pragya Agrawal
 *
 * In memory cache for certain objects that we want to cache only for the app session
 */

object CoinHoodCache {

    // cache the news since we don't want to overload the server. 
    var newsMap: MutableMap<String, CryptoPanicNews> = hashMapOf()
}
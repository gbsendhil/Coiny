package com.binarybricks.coinhood.data.database

import com.binarybricks.coinhood.data.database.entities.Coin
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.network.schedulers.BaseSchedulerProvider
import io.reactivex.Single

/**
 * Created by pranay airan on 2/3/18.
 */

class DbController(private val coinHoodDatabase: CoinHoodDatabase?) {

    fun insertCoinsInWatchList(watchedCoinList: List<WatchedCoin>,
        schedulerProvider: BaseSchedulerProvider) {
        Single.fromCallable {
            coinHoodDatabase?.watchedCoinDao()?.insertCoinsIntoWatchList(watchedCoinList)
        }.subscribeOn(schedulerProvider.io()).subscribe()
    }
}

fun getTop5CoinsToWatch(defaultExchange: String,
    defaultCurrency: String): MutableList<WatchedCoin> {
    val watchedCoin: MutableList<WatchedCoin> = mutableListOf()

    val bitcoin =
        Coin("1182", "/coins/btc/overview", "/media/19633/btc.png", "BTC", "BTC", "Bitcoin",
            "Bitcoin (BTC)", "SHA256", "PoW", "0", "21000000", "N/A", "N/A", "1", false, false)

    watchedCoin.add(WatchedCoin(bitcoin, defaultExchange, defaultCurrency, false, "0"))

    val eth =
        Coin("7605", "/coins/eth/overview", "/media/20646/eth_logo.png", "ETH", "ETH", "Ethereum",
            "Ethereum (ETH)", "Ethash", "PoW", "0", "0", "N/A", "N/A", "2", false, false)

    watchedCoin.add(WatchedCoin(eth, defaultExchange, defaultCurrency, false, "0"))

    val ripple =
        Coin("5031", "/coins/xrp/overview", "/media/19972/ripple.png", "XRP", "XRP", "Ripple",
            "Ripple (XRP)", "N/A", "N/A", "1", "38305873865", "N/A", "N/A", "12", false, false)

    watchedCoin.add(WatchedCoin(ripple, defaultExchange, defaultCurrency, false, "0"))

    val cardano =
        Coin("321992", "/coins/ada/overview", "/media/12318177/ada.png", "ADA", "ADA", "Cardano",
            "Cardano (ADA)", "Ouroboros", "PoS", "0", "45000000000", "N/A", "N/A", "1635", false,
            false)

    watchedCoin.add(WatchedCoin(cardano, defaultExchange, defaultCurrency, false, "0"))

    val litcoin =
        Coin("3808", "/coins/ltc/overview", "/media/19782/litecoin-logo.png", "LTC", "LTC",
            "Litecoin", "Litecoin (LTC)", "Scrypt", "PoW", "0", "84000000", "N/A", "N/A", "3",
            false, false)

    watchedCoin.add(WatchedCoin(litcoin, defaultExchange, defaultCurrency, false, "0"))


    return watchedCoin
}
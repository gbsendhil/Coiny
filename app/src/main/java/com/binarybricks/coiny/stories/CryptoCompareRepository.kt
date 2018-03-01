package com.binarybricks.coiny.stories

import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.CCCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.network.models.ExchangePair
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.getCoinPriceFromJson
import com.binarybricks.coiny.utils.getCoinPricesFromJson
import com.binarybricks.coiny.utils.getCoinsFromJson
import com.binarybricks.coiny.utils.getExchangeListFromJson
import io.reactivex.Single
import timber.log.Timber
import java.math.BigDecimal
import java.util.*

/**
Created by Pranay Airan 1/8/18.
 * Repository that interact with crypto api to get any info on coins.
 */

class CryptoCompareRepository(private val baseSchedulerProvider: BaseSchedulerProvider,
                              private val coinyDatabase: CoinyDatabase? = null) {

    /**
     * Get list of all supported coins
     */
    fun getAllCoins(): Single<ArrayList<CCCoin>> {
        return cryptoCompareRetrofit.create(API::class.java)
            .getCoinList()
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Coin fetched, parsing response")
                getCoinsFromJson(it)
            }
    }

    // get only price of the coin
    fun getCoinPrice(fromCurrencySymbol: String, toCurrencySymbol: String, exchange: String): Single<BigDecimal> {

        return cryptoCompareRetrofit.create(API::class.java)
            .getPrice(fromCurrencySymbol, toCurrencySymbol, exchange)
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Coin price fetched, parsing response")
                getCoinPriceFromJson(it)
            }
    }

    /**
     * Get Price and other details of Single currency to single currency, we are using the same api for multi.
     */
    fun getCoinPriceFull(fromCurrencySymbol: String, toCurrencySymbol: String): Single<CoinPrice?> {
        return if (CoinyCache.coinPriceMap.containsKey(fromCurrencySymbol)) {
            Single.just(CoinyCache.coinPriceMap[fromCurrencySymbol])
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                .getPricesFull(fromCurrencySymbol, toCurrencySymbol)
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Coin price fetched, parsing response")
                    val coinPriceList = getCoinPricesFromJson(it)
                    if (coinPriceList.size > 0) {
                        CoinyCache.coinPriceMap[fromCurrencySymbol] = coinPriceList[0]
                        coinPriceList[0]
                    } else {
                        null
                    }
                }
        }
    }

    /**
     * Get price of all currencies with respect to a specific currency
     * want data from. [fromCurrencySymbol] specifies what currencies data you want for example btc,eth.[toCurrencySymbol]
     * is which currency you want data in for like USD
     */
    fun getCoinPriceFullList(fromCurrencySymbol: String, toCurrencySymbol: String): Single<ArrayList<CoinPrice>> {

        return cryptoCompareRetrofit.create(API::class.java)
            .getPricesFull(fromCurrencySymbol, toCurrencySymbol)
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Coin price fetched, parsing response")
                getCoinPricesFromJson(it)
            }
    }


    /**
     * Get list of all supported exchanges coin pairs
     */
    fun getAllSupportedExchanges(): Single<HashMap<String, MutableList<ExchangePair>>> {

        return if (CoinyCache.coinExchangeMap.size > 0) {
            Single.just(CoinyCache.coinExchangeMap)
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                .getExchangeList()
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Exchanges fetched, parsing response")
                    val exchangeListFromJson = getExchangeListFromJson(it)
                    CoinyCache.coinExchangeMap = exchangeListFromJson
                    exchangeListFromJson
                }
        }
    }

    // insert coins in database
    fun insertCoins(coinList: MutableList<Coin>) {
        Single.fromCallable {
            coinyDatabase?.coinDao()?.insertCoins(coinList)
        }.subscribeOn(baseSchedulerProvider.io()).subscribe()
    }

    fun insertCoinsInWatchList(watchedCoinList: List<WatchedCoin>) {
        Single.fromCallable {
            coinyDatabase?.watchedCoinDao()?.insertCoinsIntoWatchList(watchedCoinList)
        }.subscribeOn(baseSchedulerProvider.io()).subscribe()
    }

    fun insertTransaction(transaction: CoinTransaction): Single<Unit?> {
        return Single.fromCallable {
            coinyDatabase?.coinTransactionDao()?.insertTransaction(transaction)
        }.subscribeOn(baseSchedulerProvider.io())
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
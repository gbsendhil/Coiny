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
import com.binarybricks.coiny.utils.*
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.math.BigDecimal
import java.util.*

/**
Created by Pranay Airan
Repository that interact with crypto api to get any info on coins.
 */

class CryptoCompareRepository(private val baseSchedulerProvider: BaseSchedulerProvider,
                              private val coinyDatabase: CoinyDatabase? = null) {

    /**
     * Get list of all supported coins
     */
    fun getAllCoins(): Single<ArrayList<CCCoin>> {

        return if (CoinyCache.coinList.size > 0) {
            Single.just(CoinyCache.coinList)
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                .getCoinList()
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Coin fetched, parsing response")
                    val coinsFromJson = getCoinsFromJson(it)
                    CoinyCache.coinList = coinsFromJson
                    coinsFromJson
                }
        }
    }

    // get only price of the coinSymbol
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
     * Get Price for specific coin to 1 or many other coins or currency.
     * This prices are for specific exchange and for specific time stamp.
     */
    fun getCoinPriceForTimeStamp(fromCoinSymbol: String, toSymbols: String, exchange: String, time: String): Single<MutableMap<String, BigDecimal>> {
        return cryptoCompareRetrofit.create(API::class.java)
            .getCoinPriceForTimeStamp(fromCoinSymbol, toSymbols, exchange, time)
            .subscribeOn(baseSchedulerProvider.io())
            .map {
                Timber.d("Coin price fetched, parsing response")
                getCoinPriceFromJsonHistorical(it)
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
     * Get list of all supported exchanges coinSymbol pairs
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

    fun getRecentTransaction(symbol: String): Flowable<List<CoinTransaction>>? {
        return coinyDatabase?.coinTransactionDao()?.getTransactionsForCoin(symbol.toUpperCase())
            ?.subscribeOn(baseSchedulerProvider.io())
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
        var quantity = transaction.quantity

        if (transaction.transactionType == TRANSACTION_TYPE_SELL) {
            quantity = quantity.multiply(BigDecimal(-1)) // since this is sell we need to decrease the quantity
        }

        return Single.fromCallable {
            coinyDatabase?.watchedCoinDao()?.updateWatchedCoinWithPurchaseQuantity(quantity, transaction.coinSymbol)
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

    watchedCoin.add(WatchedCoin(bitcoin, defaultExchange, defaultCurrency, BigDecimal.ZERO))

    val eth =
        Coin("7605", "/coins/eth/overview", "/media/20646/eth_logo.png", "ETH", "ETH", "Ethereum",
            "Ethereum (ETH)", "Ethash", "PoW", "0", "0", "N/A", "N/A", "2", false, false)

    watchedCoin.add(WatchedCoin(eth, defaultExchange, defaultCurrency, BigDecimal.ZERO))

    val ripple =
        Coin("5031", "/coins/xrp/overview", "/media/19972/ripple.png", "XRP", "XRP", "Ripple",
            "Ripple (XRP)", "N/A", "N/A", "1", "38305873865", "N/A", "N/A", "12", false, false)

    watchedCoin.add(WatchedCoin(ripple, defaultExchange, defaultCurrency, BigDecimal.ZERO))

    val cardano =
        Coin("321992", "/coins/ada/overview", "/media/12318177/ada.png", "ADA", "ADA", "Cardano",
            "Cardano (ADA)", "Ouroboros", "PoS", "0", "45000000000", "N/A", "N/A", "1635", false,
            false)

    watchedCoin.add(WatchedCoin(cardano, defaultExchange, defaultCurrency, BigDecimal.ZERO))

    val litcoin =
        Coin("3808", "/coins/ltc/overview", "/media/19782/litecoin-logo.png", "LTC", "LTC",
            "Litecoin", "Litecoin (LTC)", "Scrypt", "PoW", "0", "84000000", "N/A", "N/A", "3",
            false, false)

    watchedCoin.add(WatchedCoin(litcoin, defaultExchange, defaultCurrency, BigDecimal.ZERO))


    return watchedCoin
}
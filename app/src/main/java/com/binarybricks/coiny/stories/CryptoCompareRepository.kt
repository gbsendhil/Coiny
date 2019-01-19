package com.binarybricks.coiny.stories

import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.data.CoinyCache
import com.binarybricks.coiny.data.database.CoinyDatabase
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.Exchange
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.api.API
import com.binarybricks.coiny.network.api.cryptoCompareRetrofit
import com.binarybricks.coiny.network.models.*
import com.binarybricks.coiny.network.schedulers.BaseSchedulerProvider
import com.binarybricks.coiny.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.nio.charset.Charset

/**
Created by Pranay Airan
Repository that interact with crypto api to get any info on coins.
 */

class CryptoCompareRepository(
        private val baseSchedulerProvider: BaseSchedulerProvider,
        private val coinyDatabase: CoinyDatabase?
) {

    /**
     * Get list of all coins from api
     */
    fun getAllCoinsFromAPI(coinList: ArrayList<CCCoin>? = null, coinInfoMap: Map<String, CoinInfo>? = null): Single<Pair<ArrayList<CCCoin>, Map<String, CoinInfo>>> {

        return if (coinList != null) {
            if (coinInfoMap != null) {
                Single.just(Pair(coinList, coinInfoMap))
            } else {
                Single.just(Pair(coinList, getCoinInfoMap()))
            }
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                    .getCoinList()
                    .subscribeOn(baseSchedulerProvider.io())
                    .map {
                        Timber.d("Coin fetched, parsing response")
                        val coinsFromJson = getCoinsFromJson(it)
                        Pair(coinsFromJson, getCoinInfoMap())
                    }
        }
    }

    private fun getCoinInfoMap(): Map<String, CoinInfo> {
        val coinInfoMap = mutableMapOf<String, CoinInfo>()

        var json: String? = null
        var inputStream: InputStream? = null
        try {
            inputStream = CoinyApplication.getGlobalAppContext().assets.open("currencyinfo.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())

            val amountCurrencyType = object : TypeToken<ArrayList<CoinInfoWithCurrency>>() {
            }.type

            val coinInfoWithCurrencyList = Gson().fromJson<ArrayList<CoinInfoWithCurrency>>(json, amountCurrencyType)

            coinInfoWithCurrencyList.forEach {
                coinInfoMap[it.currencyName.toLowerCase()] = it.info
            }

            return coinInfoMap
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return coinInfoMap
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
     * Get the top coins pair by total volume for last 24 hours
     */
    fun getTopCoinsByTotalVolume24hours(tsyms: String): Single<List<CoinPrice>> {
        return if (CoinyCache.topCoinsByTotalVolume24Hours.isNotEmpty()) {
            Single.just(CoinyCache.topCoinsByTotalVolume24Hours)
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                    .getTopCoinsByTotalVolume24hours(tsyms, 10)
                    .subscribeOn(baseSchedulerProvider.io())
                    .map {
                        Timber.d("Coin price by total volume fetched, parsing response")
                        val coinPriceList = getCoinPriceListFromJson(it)
                        if (coinPriceList.size > 0) {
                            CoinyCache.topCoinsByTotalVolume24Hours = coinPriceList
                        }
                        coinPriceList
                    }
        }
    }

    /**
     * Get the top coins pair by total volume
     */
    fun getTopCoinsByTotalVolume(tsyms: String): Single<List<CoinPrice>> {
        return if (CoinyCache.topCoinsByTotalVolume.isNotEmpty()) {
            Single.just(CoinyCache.topCoinsByTotalVolume)
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                    .getTopCoinsByTotalVolume(tsyms, 20)
                    .subscribeOn(baseSchedulerProvider.io())
                    .map {
                        Timber.d("Coin price by total volume fetched, parsing response")
                        val coinPriceList = getCoinPriceListFromJson(it)
                        if (coinPriceList.size > 0) {
                            CoinyCache.topCoinsByTotalVolume = coinPriceList
                        }
                        coinPriceList
                    }
        }
    }

    /**
     * Get the top coins pair by total volume
     */
    fun getTopPairsByTotalVolume(tsyms: String): Single<List<CoinPair>> {
        return if (CoinyCache.topPairsByVolume.isNotEmpty()) {
            Single.just(CoinyCache.topPairsByVolume)
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                    .getTopPairsVolume(tsyms, 50)
                    .subscribeOn(baseSchedulerProvider.io())
                    .map {
                        Timber.d("top pair by total volume fetched, parsing response")
                        val coinPairList = getTopPairsFromJson(it)
                        if (coinPairList.size > 0) {
                            CoinyCache.topPairsByVolume = coinPairList
                        }
                        coinPairList
                    }
        }
    }

    /**
     * Get the top news article from crypto compare
     */
    fun getTopNewsFromCryptoCompare(): Single<MutableList<CryptoCompareNews>> {
        return if (CoinyCache.cyrptoCompareNews.isNotEmpty()) {
            Single.just(CoinyCache.cyrptoCompareNews)
        } else {
            cryptoCompareRetrofit.create(API::class.java)
                    .getTopNewsArticleFromCryptocompare("EN", "popular")
                    .subscribeOn(baseSchedulerProvider.io())
                    .map {
                        Timber.d("Coin news fetched from crypto compare")
                        val cryptoNews = getCryptoNewsJson(it)
                        if (cryptoNews.isNotEmpty()) {
                            if (cryptoNews.size > 20) {
                                CoinyCache.cyrptoCompareNews = cryptoNews.subList(0, 20)
                            } else {
                                CoinyCache.cyrptoCompareNews = cryptoNews
                            }
                        }
                        cryptoNews
                    }
        }
    }

    /**
     * --- Database operations --
     */

    /**
     * Get all recent transactions
     */
    fun getRecentTransaction(symbol: String): Flowable<List<CoinTransaction>>? {
        return coinyDatabase?.coinTransactionDao()?.getTransactionsForCoin(symbol.toUpperCase())
                ?.subscribeOn(baseSchedulerProvider.io())
    }

    fun insertCoinsInWatchList(watchedCoinList: List<WatchedCoin>): Single<Unit?> {
        return Single.fromCallable {
            coinyDatabase?.watchedCoinDao()?.insertCoinListIntoWatchList(watchedCoinList)
        }.subscribeOn(baseSchedulerProvider.io())
    }

    fun updateCoinWatchedStatus(watched: Boolean, coinID: String): Single<Unit?> {
        return Single.fromCallable {
            coinyDatabase?.watchedCoinDao()?.makeCoinWatched(watched, coinID)
        }.subscribeOn(baseSchedulerProvider.io())
    }

    fun insertTransaction(transaction: CoinTransaction): Single<Unit?> {
        var quantity = transaction.quantity

        if (transaction.transactionType == TRANSACTION_TYPE_SELL) {
            quantity = quantity.multiply(BigDecimal(-1)) // since this is sell we need to decrease the quantity
        }

        return Single.fromCallable {
            coinyDatabase?.watchedCoinDao()?.addPurchaseQuantityForCoin(quantity, transaction.coinSymbol)
            coinyDatabase?.coinTransactionDao()?.insertTransaction(transaction)
        }.subscribeOn(baseSchedulerProvider.io())
    }

    /**
     * Get list of all coins with there watched status
     */
    fun getAllCoins(): Flowable<List<WatchedCoin>>? {
        coinyDatabase?.let {
            return it.watchedCoinDao().getAllCoins().subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }

    /**
     * Get single coin based on coin name and symbol
     */
    fun getSingleCoin(symbol: String): Single<List<WatchedCoin>>? {
        coinyDatabase?.let {
            return it.watchedCoinDao().getSingleWatchedCoin(symbol).subscribeOn(baseSchedulerProvider.io())
        }
        return null
    }

    fun insertExchangeIntoList(exchangeList: List<Exchange>): Single<Unit?> {
        return Single.fromCallable {
            coinyDatabase?.exchangeDao()?.insertExchanges(exchangeList)
        }.subscribeOn(baseSchedulerProvider.io())
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

    /**
     * Get exchange details and save in DB
     */
    fun getExchangeInfo(): Single<List<Exchange>> {
        return cryptoCompareRetrofit.create(API::class.java)
                .getExchangesInfo()
                .subscribeOn(baseSchedulerProvider.io())
                .map {
                    Timber.d("Exchanges info fetched, parsing response")
                    getExchangeInfo(it)
                }
    }
}

fun getTop5CoinsToWatch(): MutableList<String> {
    val watchedCoin: MutableList<String> = mutableListOf()

    val bitcoin = "1182"
    watchedCoin.add(bitcoin)

    val eth = "7605"
    watchedCoin.add(eth)

    val ripple = "5031"
    watchedCoin.add(ripple)

    val eos = "166503"
    watchedCoin.add(eos)

    val litcoin = "3808"
    watchedCoin.add(litcoin)

    return watchedCoin
}
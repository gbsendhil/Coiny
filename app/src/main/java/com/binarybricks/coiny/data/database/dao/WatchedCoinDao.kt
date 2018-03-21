package com.binarybricks.coiny.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import io.reactivex.Flowable
import java.math.BigDecimal

/**
 * Created by Pragya Agrawal
 *
 * Add queries to read/update coinSymbol data from database.
 */
@Dao
interface WatchedCoinDao {

    @Query("select * from WatchedCoin where purchaseQuantity > 0 OR watched = :watched order by purchaseQuantity")
    fun getAllWatchedCoins(watched: Boolean = true): Flowable<List<WatchedCoin>>

    @Query("select * from WatchedCoin order by sortOrder")
    fun getAllCoins(): Flowable<List<WatchedCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinsIntoWatchList(list: List<WatchedCoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinIntoWatchList(watchedCoin: WatchedCoin)

    @Query("update WatchedCoin set purchaseQuantity = purchaseQuantity + :quantity where symbol=:symbol")
    fun updateWatchedCoinWithPurchaseQuantity(quantity: BigDecimal, symbol: String): Int


    @Query("UPDATE WatchedCoin SET watched = :watched  WHERE coinId = :coinId")
    fun updateWatchedCoinAddCoinToWatchlist(watched: Boolean, coinId: String)
}
